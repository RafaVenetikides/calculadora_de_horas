package rafael.venetikides.calc_backend.classes;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

public class Periodo implements Iterable<Horario>{

    ArrayList<Horario> marcacoes;
    Duration cargaHoraria;
    Duration tolerance;

    /*
     * Construtor da classe de Periodos.
     * Uma lista de Horarios marcados no dia.
     */
    public Periodo(Duration cargaHoraria){
        this.marcacoes = new ArrayList<>();
        this.cargaHoraria = cargaHoraria;
        this.tolerance = Duration.ofMinutes(10);
    }

    public ArrayList<Horario> getMarcacoes() {
        return marcacoes;
    }

    public Duration getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Duration cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Duration getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = Duration.ofMinutes(tolerance);
    }
    
    public void addMarcacao(Integer hora, Integer minuto){
        int lastIndex = marcacoes.size() - 1;
        if(marcacoes.isEmpty()){
            marcacoes.add(criaHorario(hora, minuto, 1, 1, 1));
        } else if(hora <= marcacoes.get(lastIndex).getMarcacao().getHour() || (hora == marcacoes.get(lastIndex).getMarcacao().getHour() && minuto <= marcacoes.get(lastIndex).getMarcacao().getMinute())){
            LocalDate proximoDia = marcacoes.get(lastIndex).getMarcacao().toLocalDate().plusDays(1);
            marcacoes.add(criaHorario(hora, minuto, proximoDia.getDayOfMonth(), proximoDia.getMonthValue(), proximoDia.getYear()));
        } else{
            LocalDate diaAtual = marcacoes.get(lastIndex).getMarcacao().toLocalDate();
            marcacoes.add(criaHorario(hora, minuto, diaAtual.getDayOfMonth(), diaAtual.getMonthValue(), diaAtual.getYear()));
        }
    }

    public Horario criaHorario(Integer hora, Integer minuto, Integer dia, Integer mes, Integer ano){
        LocalDateTime m = LocalDateTime.of(ano, mes, dia, hora, minuto);

		return new Horario(m);
    }

    /*
     * Ordena os valores das marcacoes de acordo com o dia e hora da marcação
     */
    public void ordenaPeriodo(){
        Collections.sort(marcacoes);
    }

    @Override
    public Iterator<Horario> iterator(){
        return marcacoes.iterator();
    }

    /*
     * toString() para impressão no terminal
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Iterator<Horario> i = iterator();
        while(i.hasNext()){
            Horario horario = i.next();
            s.append(horario.hourToString()); 

            if(i.hasNext()){ 
                s.append(" | ");
            }
        }
        s.append("\n");

        Iterator<Horario> j = iterator();
        while(j.hasNext()){
            Horario horario = j.next();
            s.append(horario.dataToString());

            if(j.hasNext()){
                s.append(" | ");
            }
        }

        return s.toString();
    }

    /*
     * Calcula as horas trabalhadas de acordo com as marcações
     * Serão somente calculadas as horas trabalhadas em períodos fechados, com entrada e saída
     */
    public Duration calculaHorasTrabalhadas(){

        Iterator<Horario> i = iterator();
        Duration horasTrabalhadas = Duration.ZERO;
        Duration essePeriodo;
    
        while(i.hasNext()){
            Horario entrada = i.next();
            
            if(i.hasNext()){
                Horario saida = i.next();
                
                essePeriodo = Duration.between(entrada.getMarcacao(), saida.getMarcacao());
                horasTrabalhadas = horasTrabalhadas.plus(essePeriodo);
            }
        }
        return horasTrabalhadas;
    }
    
    /*
     * Calcula o Saldo de horas respeitando a tolerância de 10 minutos.
     * Caso tenha até 10 minutos a mais ou a menos, retorna a carga horária
     * Se tiver feito hora extra ou estar devendo horas, calcula esse valor e retorna
     */
    public Duration calculaSaldo(){
        Duration horasTrabalhadas = calculaHorasTrabalhadas();

        if (horasTrabalhadas.compareTo(cargaHoraria.minus(tolerance)) < 0){
            return cargaHoraria.minus(horasTrabalhadas);

        } else if(horasTrabalhadas.compareTo(cargaHoraria.plus(tolerance)) > 0){
            return horasTrabalhadas.minus(cargaHoraria);

        } else{
            return Duration.ZERO;
        }
    }

    /*
     * Utiliza o valor calculado de calculaSaldo() para retornar se foi um crédito ou um Débito.
     * Utilizado no debug para saber se o saldo está correto
     */
    public String getSaldo(){
        StringBuilder s = new StringBuilder();
        Duration saldo = calculaSaldo();

        if (calculaHorasTrabalhadas().compareTo(cargaHoraria) >= 0){
            s.append("Credito: ").append(saldo);
        } else{
            s.append("Débito: ").append(saldo);
        }

        return s.toString();
    }

    /*
     * Calcula o crédito de horas
     * Se a quantidade de horas trabalhadas for maior ou igual a carga horária, retorna o saldo
     * Caso contrário, retorna 0
     */
    public Duration getCredito(){
        Duration saldo = calculaSaldo();
        if (calculaHorasTrabalhadas().compareTo(cargaHoraria) >= 0){
            return saldo;
        } else{
            return Duration.ZERO;
        }
    }

    /*
     * Calcula o débito de horas
     * Se a quantidade de horas trabalhadas for menor que a carga horária, retorna o saldo
     * Caso contrário, retorna 0
     */
    public Duration getDebito(){
        Duration saldo = calculaSaldo();
        if (calculaHorasTrabalhadas().compareTo(cargaHoraria) < 0){
            return saldo;
        } else{
            return Duration.ZERO;
        }
    }

    /*
     * Calcula o tempo de intervalo
     * Primeiro procura aonde está a saída e calcula o tempo até a próxima entrada
     * Retorna um Duration sendo a soma de todos os intervalos
     */
    public Duration calculaIntervalo(){
        Duration intervalo = Duration.ZERO;
        Duration esseIntervalo;
        ListIterator<Horario> i = marcacoes.listIterator();

        while(i.hasNext()){
            i.next();
            if(i.hasNext()){
                Horario entradaIntervalo = i.next();

                if(i.hasNext()){
                    Horario saidaIntervalo = i.next();

                    esseIntervalo = Duration.between(entradaIntervalo.getMarcacao(), saidaIntervalo.getMarcacao());
                    intervalo = intervalo.plus(esseIntervalo);
                    i.previous();
                }
            }
        }
        
        return intervalo;
    }

    /*
     * Calcula o adicional noturno
     * Analisa as jornadas de trabalho que acontecem em período noturno (entre 22:00 e 5:00).
     * Passa por todas as possibilidade de marcações e calcula o tempo noturno de cada uma.
     * Realiza a operação de cálculo de tempo noturno e retorna um Duration com o valor calculado.
     * A função poderia ter sido refatorada para ficar menor, porém optei por deixar mais legível, além de facilitar o debug.
     */

     public Duration calculaAdicionalNoturno() {
        Duration tempoNoturno = Duration.ZERO;
        LocalTime inicioNoturno = LocalTime.of(22, 0);
        LocalTime fimNoturno = LocalTime.of(5, 0);
        
        ListIterator<Horario> i = marcacoes.listIterator();
    
        while (i.hasNext()) {
            Horario entrada = i.next();
            if (i.hasNext()) {
                Horario saida = i.next();
    
                LocalDateTime inicioJornada = entrada.getMarcacao();
                LocalDateTime fimJornada = saida.getMarcacao();


                // Caso 1: A jornada se inicia antes das 5:00
                if (inicioJornada.toLocalTime().isBefore(fimNoturno)) {

                    // Caso 1.1: A jornada termina antes das 5:00
                    if(fimJornada.isBefore(LocalDateTime.of(inicioJornada.toLocalDate(), fimNoturno))){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada.toLocalTime(), fimJornada));
                    }

                    // Caso 1.2: a jornada termina depois das 5:00
                    else if(fimJornada.toLocalTime().isAfter(fimNoturno) && fimJornada.toLocalTime().isBefore(inicioNoturno)){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada.toLocalTime(), fimNoturno));
                    }

                    // Caso 1.3: A jornade termina no próximo horário noturno
                    else if (fimJornada.toLocalTime().isAfter(inicioNoturno) || fimJornada.toLocalTime().isBefore(inicioJornada.toLocalTime())) {
                        // Calcula o tempo noturno do primeiro dia (entre o início e as 5:00)
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, LocalDateTime.of(inicioJornada.toLocalDate(), fimNoturno)));
                        if(fimJornada.toLocalDate().isAfter(inicioJornada.toLocalDate())){
                        // Calcula o tempo noturno do segundo dia (entre 22:00 e o horário de término no próximo dia)
                            tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(fimJornada.toLocalDate().minusDays(1), inicioNoturno), fimJornada));
                        } else{
                            tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(inicioJornada.toLocalDate(), inicioNoturno), fimJornada));
                        }
                    }
                }

                // Caso 2: A jornada se inicia antes das 22:00
                else if(inicioJornada.toLocalTime().isBefore(inicioNoturno)){

                    // Caso 2.0: A jornada termina antes das 22:00
                    if (fimJornada.isBefore(LocalDateTime.of(inicioJornada.toLocalDate(), inicioNoturno)) && fimJornada.toLocalTime().isAfter(fimNoturno)){
                        continue;
                    }
                    
                    // Caso 2.1: A jornada termina depois das 22:00 e antes das 5:00 (cruza para o dia seguinte)
                    if (fimJornada.toLocalTime().isAfter(inicioNoturno) || fimJornada.toLocalTime().isBefore(fimNoturno) || fimJornada.toLocalTime().equals(fimNoturno)) {
                        
                        // Se a jornada cruzar para o dia seguinte (fimJornada é antes das 5:00 do dia seguinte)
                        if (fimJornada.toLocalTime().isBefore(fimNoturno) || fimJornada.toLocalTime().equals(fimNoturno)) {
                            // Calcula o tempo noturno entre 22:00 do dia atual e 5:00 do dia seguinte
                            tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(inicioJornada.toLocalDate(), inicioNoturno), fimJornada));
                        } 
                        // Se a jornada ainda estiver no mesmo dia mas entre 22:00 e 5:00
                        else if (fimJornada.toLocalTime().isAfter(inicioNoturno)) {
                            tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(inicioJornada.toLocalDate(), inicioNoturno), fimJornada));
                        }
                    }
                    
                    // Caso 2.2: A jornada termina depois das 5:00
                    else if(fimJornada.toLocalTime().isAfter(fimNoturno) && !fimJornada.toLocalTime().equals(inicioNoturno)){
                        tempoNoturno = tempoNoturno.plus(Duration.ofHours(7));
                    }
                }

                // Caso 3: A jornada se inicia depois das 22:00
                else if (inicioJornada.toLocalTime().equals(inicioNoturno) || inicioJornada.toLocalTime().isAfter(inicioNoturno)) {

                    // Caso 3.1: A jornada termina no mesmo dia
                    if(inicioJornada.toLocalDate().equals(fimJornada.toLocalDate())){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, fimJornada));
                    }

                    // Caso 3.2: A jornada termina antes das 5:00
                    else if ((fimJornada.toLocalTime().isBefore(fimNoturno)) || fimJornada.toLocalTime().equals(fimNoturno)) {
                        // A jornada começa após as 22:00 e termina antes das 5:00 do próximo dia
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, fimJornada));
                    }

                    // Caso 3.3: A jornada termina depois das 5:00 do dia seguinte
                    else if (fimJornada.toLocalTime().isAfter(fimNoturno) && fimJornada.toLocalTime().isBefore(inicioNoturno)) {
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, LocalDateTime.of(fimJornada.toLocalDate(), fimNoturno)));
                    }

                    // Caso 3.4: A jornada termina depois das 22:00 em outro dia
                    else if (fimJornada.toLocalTime().isAfter(inicioNoturno) || fimJornada.toLocalTime().equals(inicioNoturno)) {
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, LocalDateTime.of(inicioJornada.toLocalDate().plusDays(1), fimNoturno)));
                        tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(fimJornada.toLocalDate(), inicioNoturno), fimJornada));
                    }
                }
            
            }
        }        

        Integer totalMinutosNoturnos = (int) tempoNoturno.toMinutes();        
        double horasEquivalentes = totalMinutosNoturnos / 52.5;
        long minutosAdicionalNoturno = (long) (horasEquivalentes * 60);

        return Duration.ofMinutes(minutosAdicionalNoturno);
    }

    public static String durationToString(Duration duration){
        long totalMinutes = duration.toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
    

