package rafael.venetikides.calc_backend.classes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Periodo implements Iterable<Horario>{
    ArrayList<Horario> marcacoes;

    /*
     * Construtor da classe de Periodos.
     * Uma lista de Horarios marcados no dia.
     */
    public Periodo(){
        this.marcacoes = new ArrayList<>();
    }

    public ArrayList<Horario> getMarcacoes() {
        return marcacoes;
    }
    
    public void addMarcacao(Horario horario){
        marcacoes.add(horario);
    }

    public void sortPeriodo(){
        Collections.sort(marcacoes);
    }

    @Override
    public Iterator<Horario> iterator(){
        return marcacoes.iterator();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Iterator<Horario> i = iterator();
        while(i.hasNext()){
            Horario horario = i.next(); // Get the next Horario object
            s.append(horario.toString()); // Append the Horario's string representation
            
            if(i.hasNext()){ // Add a separator if there's another Horario
                s.append(" | ");
            }
        }
        return s.toString();
    }
    
}
