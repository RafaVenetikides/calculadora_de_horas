package rafael.venetikides.calc_backend.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rafael.venetikides.calc_backend.classes.Periodo;

@RestController
@RequestMapping("/api/v1/periodo")  // Define o caminho base para a API
@CrossOrigin(origins = "http://localhost:5000")

public class PeriodoController {

    private final Periodo periodo;

    // Construtor para inicializar o período com uma carga horária
    public PeriodoController() {
        // Inicialize o período com uma carga horária de 8 horas, por exemplo
        this.periodo = new Periodo(Duration.ofHours(8));
    }

    // Endpoint para retornar as marcações do período
    @GetMapping("/marcacoes")
    public ResponseEntity<Map<String, Object>> getMarcacoes() {
        Map<String, Object> response = new HashMap<>();
        response.put("marcacoes", periodo.getMarcacoes());
        return ResponseEntity.ok(response);
    }

    // Endpoint para adicionar uma nova marcação
    @PostMapping("/add-marcacao")
    public ResponseEntity<String> addMarcacao(@RequestBody Map<String, Integer> marcacao) {
        int hora = marcacao.get("hora");
        int minuto = marcacao.get("minuto");
        periodo.addMarcacao(hora, minuto);
        return ResponseEntity.ok("Marcação adicionada com sucesso!");
    }

    // Endpoint para calcular o saldo de horas
    @GetMapping("/saldo")
    public ResponseEntity<Map<String, Object>> getSaldo() {
        Map<String, Object> response = new HashMap<>();
        response.put("saldo", periodo.getSaldo());
        return ResponseEntity.ok(response);
    }

    // Endpoint para calcular o adicional noturno
    @GetMapping("/adicional-noturno")
    public ResponseEntity<Map<String, Object>> getAdicionalNoturno() {
        Map<String, Object> response = new HashMap<>();
        response.put("adicionalNoturno", periodo.calculaAdicionalNoturno().toMinutes());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calcular")
    public ResponseEntity<Map<String, String>> calcular(@RequestBody Map<String, Object> payload) {
        String cargaHoraria = (String) payload.get("cargaHoraria");
        List<String> marcacoes = (List<String>) payload.get("marcacoes");


        Periodo periodo = new Periodo(parseDuration(cargaHoraria));
        for (String marcacao : marcacoes) {
            String[] time = marcacao.split(":");
            periodo.addMarcacao(Integer.valueOf(time[0]), Integer.valueOf(time[1]));
        }

        Map<String, String> resultados = new HashMap<>();
        resultados.put("horasTrabalhadas", Periodo.durationToString(periodo.calculaHorasTrabalhadas()));
        resultados.put("debito", Periodo.durationToString(periodo.getDebito()));
        resultados.put("credito", Periodo.durationToString(periodo.getCredito()));
        resultados.put("adicionalNoturno", Periodo.durationToString(periodo.calculaAdicionalNoturno()));
        resultados.put("intervalo", Periodo.durationToString(periodo.calculaIntervalo()));

        return ResponseEntity.ok(resultados);
    }

    public Duration parseDuration(String time) {
        String[] parts = time.split(":");
        try{
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        
        return Duration.ofHours(hours).plusMinutes(minutes);
        } catch (Exception e) {
            return Duration.ZERO;
        }
    }
}
