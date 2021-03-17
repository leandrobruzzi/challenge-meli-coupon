package ar.com.meli.coupon.service.impl;

import ar.com.meli.coupon.dto.CalculateCouponRequestDto;
import ar.com.meli.coupon.dto.CalculateCouponResponseDto;
import ar.com.meli.coupon.dto.CombinationDto;
import ar.com.meli.coupon.dto.ItemDto;
import ar.com.meli.coupon.exceptions.InsufficientAmountException;
import ar.com.meli.coupon.exceptions.MaxCominationException;
import ar.com.meli.coupon.exceptions.ValueGreaterThanAllowedAmountException;
import ar.com.meli.coupon.service.CouponService;
import ar.com.meli.coupon.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouponServiceImpl implements CouponService {

    private final Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    private Messages messages;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${meli.url.base}")
    private String meliUrlBase;

    @Override
    public CalculateCouponResponseDto calculateProducts(CalculateCouponRequestDto calculateCouponRequestDto) throws InsufficientAmountException {
        CombinationDto maxCombination = new CombinationDto();
        logger.info("Armando set de datos...");
        Map<String, Float> items = findItems(calculateCouponRequestDto.getItem_ids());
        List<String> itemsResponse = calculate(items, calculateCouponRequestDto.getAmount(), maxCombination);
        return new CalculateCouponResponseDto(itemsResponse, maxCombination.getValue());
    }

    private Map<String, Float> findItems(List<String> item_ids) {
        //Consumo servicio para obtener el valor de los items.
        Map<String, Float> items = new HashMap<>();
        logger.info("Consultando valor de productos...");
        for (int i = 0; i<item_ids.size(); i++){
            try{
                ResponseEntity<ItemDto> response = restTemplate.getForEntity(meliUrlBase + "/items/" + item_ids.get(i).replace("-",""), ItemDto.class);
                items.put(item_ids.get(i), response.getBody().getPrice());
            }catch (Exception e){
                //En este caso decidi no penalizar el calculo del cupon ya que al ingresar los valores a mano es probable que alguno este mal escrito
                //Pero podria explotar la exception en este punto y cortar el proceso informando Ocurrio un error intentelo mas tarde
                logger.error(messages.get("cuponservice.failed.find.product"), e);
            }
        }

        return items;
    }

    /**
     * Realizo la combinacion de los productos de forma recursiva
     * @param input
     * @param idx
     * @param options
     * @param amount
     * @throws MaxCominationException
     */
    private void findValue(List<Map.Entry<String, Float>> input, int idx, List<Map.Entry<String, Float>> options, Float amount, CombinationDto maxCombination) throws MaxCominationException {
        try {
            for (int i = idx + 1; i < options.size(); i++) {
                List<Map.Entry<String, Float>> output = new ArrayList<>(input);

                output.add(options.get(i));
                logger.debug(output.toString());
                verificateMaxCombination(output, amount, maxCombination);

                findValue(output, ++idx, options, amount, maxCombination);
            }
        }catch (ValueGreaterThanAllowedAmountException e){
            //En este punto no tiene sentido seguir con las combinaciones siguientes porque siempre seran mayores al monto.
            logger.debug(e.getMessage());
        }
    }

    @Override
    public List<String> stressTest(Map<String, Float> items, Float amount, CombinationDto maxCombination) throws InsufficientAmountException {
        return calculate(items,amount,maxCombination);
    }

    private List<String> calculate(Map<String, Float> items, Float amount, CombinationDto maxCombination) throws InsufficientAmountException {
        //Transformo el mapa a una Lista de Entrys
        List<Map.Entry<String, Float>> itemsList = new ArrayList<>(items.entrySet());

        //Ordeno la lista de items de menor a mayor
        itemsList.sort(Map.Entry.comparingByValue());

        //Filtro los items que superan el monto permitido, si encuentro uno con valor exacto solo retorno ese.
        itemsList = filerGreaterThanAmount(itemsList, amount);

        //Si hay un solo elemento significa que hay un item que gasta el total del monto
        List<Map.Entry<String, Float>> initialCombination = new ArrayList<Map.Entry<String, Float>>();
        if(itemsList.size() == 0) {
            throw new InsufficientAmountException();
        }else if(itemsList.size() == 1){
            initialCombination.add(itemsList.get(0));
            maxCombination.setCombination(initialCombination);
            return getListOfItems(maxCombination);
        }

        //Cargo el item con mayor valor por si todas las combinaciones de dos items superan el monto permitido
        initialCombination.add(itemsList.get(itemsList.size() - 1));
        maxCombination.setCombination(initialCombination);

        //Recorro los items y realizo la combinatoria completa (Poco eficiente)
        //TODO: Buscar la forma de hacer este proceso mas eficiente, a mil registros cuelgo el servidor.
        try{
            logger.info("Lista de items a trabajar: " + itemsList.toString());
            logger.info("Calculando mejor combinacion de productos...");
            for (int i=0; i<itemsList.size(); i++){
                logger.debug("Valor a combinar: " + itemsList.get(i).getKey() + "=" + itemsList.get(i).getValue());
                List<Map.Entry<String, Float>> input = new ArrayList<>();
                input.add(itemsList.get(i));
                findValue(input, i, itemsList, amount, maxCombination);
            }
        }catch (MaxCominationException e){
            logger.info("Se encontro una combinacion exacta!");
        }

        //Retorno la combinacion de productos mas aproxima al monto permitido
        logger.info(String.format("Mejor combinacion encontrada: %s - amount: %s", maxCombination, amount));
        return getListOfItems(maxCombination);
    }

    /**
     * Verifico si la combinacion encontrada es superior a la que tengo como maxima, de ser asi me quedo con la nueva
     * @param output
     * @param amount
     */
    private void verificateMaxCombination(List<Map.Entry<String, Float>> output, Float amount, CombinationDto maxCombination) throws MaxCominationException, ValueGreaterThanAllowedAmountException {
        float val = 0;
        for (Map.Entry<String, Float> item : output) {
            val = val + item.getValue();
        }
        if(val > amount){
            throw new ValueGreaterThanAllowedAmountException(messages.get("cuponservice.valuegreaterthanallowedamount"));
        }
        if(val > maxCombination.getValue()){
            maxCombination.setCombination(output);
        }
        if(maxCombination.getValue().equals(amount)){
            throw new MaxCominationException();
        }
    }

    /**
     * Filtro la lista de items para quedarme con los elementos de valor menor o igual al monto permitido
     * @param itemsList
     * @param amount
     * @return Lista de items filtrada
     */
    private List<Map.Entry<String,Float>> filerGreaterThanAmount(List<Map.Entry<String, Float>> itemsList, Float amount) {
        List<Map.Entry<String,Float>> newItemList = new ArrayList<>();
        for (int i=0; i< itemsList.size(); i++){
            Map.Entry<String, Float> item = itemsList.get(i);

            //Agrego a la nueva lista los elementos que valor menos al monto permitido
            if(item.getValue() <= amount){
                //Si encuentro uno con el valor exacto retorno la lista con ese unico elemento
                if(item.getValue().equals(amount)){
                    newItemList.clear();
                    newItemList.add(item);
                    break;
                }
                newItemList.add(item);
            }
        }

        return newItemList;
    }

    /**
     * Transformo una Cominacion a una Lista de String
     * @param combination
     * @return Lista de strings con las keys de los productos
     */
    private List<String> getListOfItems(CombinationDto combination) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Float> entry : combination.getCombination()) {
            result.add(entry.getKey());
        }
        return result;
    }

}
