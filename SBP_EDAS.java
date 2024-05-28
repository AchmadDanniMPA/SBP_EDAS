import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
class House{
    String name;
    double[] scores;
    House(String name, double[] scores){
        this.name=name;
        this.scores=scores;
    }
}
class Criteria{
    String name;
    double weight;
    boolean isBenefit;
    Criteria(String name, double weight, boolean isBenefit){
        this.name=name;
        this.weight=weight;
        this.isBenefit=isBenefit;
    }
}
class EDAS{
    List<House> houses;
    List<Criteria> criteria;
    double[] AV;
    EDAS(List<House> houses, List<Criteria> criteria){
        this.houses=houses;
        this.criteria=criteria;
        this.AV=new double[criteria.size()];
        calculateAV();
    }
    private void calculateAV(){
        for(int i=0; i<criteria.size(); i++){
            double sum=0;
            for(House house:houses){
                sum+=house.scores[i];
            }
            AV[i]=sum/houses.size();
        }
    }
    public void calculateEDAS(){
        double[][] PDA=new double[houses.size()][criteria.size()];
        double[][] NDA=new double[houses.size()][criteria.size()];
        double[] SP=new double[houses.size()];
        double[] SN=new double[houses.size()];
        double[] NSP=new double[houses.size()];
        double[] NSN=new double[houses.size()];
        double[] AS=new double[houses.size()];
        for(int i=0; i<houses.size(); i++){
            for(int j=0; j<criteria.size(); j++){
                if(criteria.get(j).isBenefit){
                    PDA[i][j]=Math.max(0,(houses.get(i).scores[j]-AV[j])/AV[j]);
                    NDA[i][j]=Math.max(0,(AV[j]-houses.get(i).scores[j])/AV[j]);
                }else{
                    PDA[i][j]=Math.max(0,(AV[j]-houses.get(i).scores[j])/AV[j]);
                    NDA[i][j]=Math.max(0,(houses.get(i).scores[j]-AV[j])/AV[j]);
                }
            }
        }
        for(int i=0; i<houses.size(); i++){
            for(int j=0; j<criteria.size(); j++){
                SP[i]+=criteria.get(j).weight*PDA[i][j];
                SN[i]+=criteria.get(j).weight*NDA[i][j];
            }
        }
        double maxSP=getMax(SP);
        double maxSN=getMax(SN);
        for(int i=0; i<houses.size(); i++){
            NSP[i]=SP[i]/maxSP;
            NSN[i]=1-(SN[i]/maxSN);
        }
        for(int i=0; i<houses.size(); i++){
            AS[i]=0.5*(NSP[i]+NSN[i]);
        }
        displayResults(AS);
    }
    private double getMax(double[] array){
        double max=array[0];
        for(double v:array){
            if(v>max){
                max=v;
            }
        }
        return max;
    }
    private void displayResults(double[] AS){
        List<Result> results=new ArrayList<>();
        for(int i=0; i<houses.size(); i++){
            results.add(new Result(houses.get(i).name, AS[i]));
        }
        results.sort(Comparator.comparingDouble(Result::getAS).reversed());
        System.out.println("Ranking dari EDAS:");
        for(int i=0; i<results.size(); i++){
            System.out.println((i+1)+". "+results.get(i).name+"-AS: "+results.get(i).AS);
        }
        System.out.println("Jadi rumah terbaik menurut EDAS adalah "+results.get(0).name);
    }
    static class Result{
        String name;
        double AS;
        Result(String name, double AS){
            this.name=name;
            this.AS=AS;
        }
        public double getAS(){
            return AS;
        }
    }
}
public class SBP_EDAS{
    public static void main(String[] args) {
        List<House> houses=new ArrayList<>();
        houses.add(new House("A1", new double[]{7, 7, 9, 7, 1, 9, 1, 6}));
        houses.add(new House("A2", new double[]{4, 3, 5, 3, 3, 10, 7, 4}));
        houses.add(new House("A3", new double[]{5, 5, 7, 4, 9, 2, 6, 1}));
        houses.add(new House("A4", new double[]{9, 5, 10, 9, 7, 8, 6, 7}));
        houses.add(new House("A5", new double[]{10, 1, 9, 2, 10, 6, 10, 2}));
        houses.add(new House("A6", new double[]{6, 7, 2, 7, 10, 2, 8, 7}));
        houses.add(new House("A7", new double[]{10, 9, 10, 2, 6, 7, 10, 4}));
        houses.add(new House("A8", new double[]{7, 6, 6, 4, 7, 2, 3, 9}));
        houses.add(new House("A9", new double[]{4, 9, 9, 1, 6, 1, 6, 1}));
        houses.add(new House("A10", new double[]{9, 8, 2, 4, 5, 3, 2, 5}));
        houses.add(new House("A11", new double[]{4, 4, 2, 5, 4, 10, 6, 5}));
        houses.add(new House("A12", new double[]{10, 6, 9, 9, 10, 10, 2, 6}));
        houses.add(new House("A13", new double[]{6, 8, 8, 5, 4, 9, 2, 3}));
        houses.add(new House("A14", new double[]{8, 6, 3, 3, 6, 7, 8, 7}));
        houses.add(new House("A15", new double[]{4, 9, 3, 5, 10, 5, 10, 6}));
        houses.add(new House("A16", new double[]{6, 2, 7, 6, 5, 6, 5, 9}));
        houses.add(new House("A17", new double[]{8, 4, 1, 8, 3, 10, 7, 4}));
        houses.add(new House("A18", new double[]{4, 1, 8, 5, 10, 5, 2, 6}));
        houses.add(new House("A19", new double[]{9, 8, 9, 5, 1, 3, 10, 6}));
        houses.add(new House("A20", new double[]{3, 1, 10, 4, 7, 5, 10, 8}));
        List<Criteria> criteria=new ArrayList<>();
        criteria.add(new Criteria("K1", 0.2904, true));
        criteria.add(new Criteria("K2", 0.2133, false));
        criteria.add(new Criteria("K3", 0.1708, true));
        criteria.add(new Criteria("K4", 0.0441, true));
        criteria.add(new Criteria("K5", 0.1399, true));
        criteria.add(new Criteria("K6", 0.0293, true));
        criteria.add(new Criteria("K7", 0.0624, true));
        criteria.add(new Criteria("K8", 0.0498, true));
        EDAS edas=new EDAS(houses, criteria);
        edas.calculateEDAS();
    }
}
