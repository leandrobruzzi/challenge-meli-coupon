package ar.com.meli.coupon;

import ar.com.meli.coupon.exceptions.MaxCominationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private final Logger logger = LoggerFactory.getLogger(Main.class);

    private Combination maxCombination = new Combination();

    public static void main(String[] args) {
        Main m = new Main();
        m.init();
    }

    private void init(){
        logger.info("Armando set de datos...");
        //Armo un set de datos random para probar
        Map<String, Float> items = new HashMap<>();
        for (int i = 0; i<20; i++){
            items.put("MLA"+String.valueOf(i), (float) (Math.round( (Math.random()*(500-1)+1) * 100 ) / 100d) );
        }
        items.put("MLA1", Float.valueOf(100));
        items.put("MLA2", Float.valueOf(210));
        items.put("MLA3", Float.valueOf(260));
        items.put("MLA4", Float.valueOf(30));
        items.put("MLA5", Float.valueOf(90));

        logger.info("Calculando mejor combinacion de productos...");
        calculate(items, Float.valueOf(500));
    }

    private List<String> calculate(Map<String, Float> items, Float amount){
        //Transformo el mapa a una Lista de Entrys
        List<Map.Entry<String, Float>> itemsList = new ArrayList<>(items.entrySet());

        //Ordeno la lista de items de menor a mayor
        itemsList.sort(Map.Entry.comparingByValue());

        //Filtro los items que superan el monto permitido, si encuentro uno con valor exacto solo retorno ese.
        itemsList = filerGreaterThanAmount(itemsList, amount);

        //Si hay un solo elemento significa que hay un item que gasta el total del monto
        if(itemsList.size() == 1){
            return getListOfItems(new Combination(itemsList));
        }

        //Recorro los items y realizo la combinatoria completa (Poco eficiente)
        //TODO: Buscar la forma de hacer este proceso mas eficiente, a mil registros cuelgo el servidor.
        try{
            for (int i=0; i<itemsList.size(); i++){
                List<Map.Entry<String, Float>> input = new ArrayList<>();
                input.add(itemsList.get(i));
                findValue(input, i, itemsList, amount);
            }
        }catch (MaxCominationException e){
            logger.info("Se encontro una combinacion exacta!");
        }

        //Retorno la combinacion de productos mas aproxima al monto permitido
        logger.info(String.format("Mejor combinacion encontrada: %s - amount: %s", maxCombination, amount));
        return getListOfItems(maxCombination);
    }

    /**
     * Realizo la combinacion de los productos de forma recursiva
     * @param input
     * @param idx
     * @param options
     * @param amount
     * @throws MaxCominationException
     */
    private void findValue(List<Map.Entry<String, Float>> input, int idx, List<Map.Entry<String, Float>> options, Float amount) throws MaxCominationException {
        for(int i = idx+1 ; i < options.size(); i++) {
            List<Map.Entry<String, Float>> output = new ArrayList<>(input);
            output.add(options.get(i));
            logger.info(output.toString());
            verificateMaxCombination(output, amount);

            findValue(output,++idx, options, amount);
        }
    }

    /**
     * Verifico si la combinacion encontrada es superior a la que tengo como maxima, de ser asi me quedo con la nueva
     * @param output
     * @param amount
     */
    private void verificateMaxCombination(List<Map.Entry<String, Float>> output, Float amount) throws MaxCominationException {
        float val = 0;
        for (Map.Entry<String, Float> item : output) {
            val = val + item.getValue();
        }
        if(val > maxCombination.getValue() && val <= amount ){
            maxCombination = new Combination(output);
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
    private List<String> getListOfItems(Combination combination) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Float> entry : combination.getCombination()) {
            result.add(entry.getKey());
        }
        return result;
    }

}
