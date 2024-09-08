package rafael.venetikides.calc_backend;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import rafael.venetikides.calc_backend.classes.Periodo;

@SpringBootApplication
public class CalcBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalcBackendApplication.class, args);

		Duration cargaHoraria = Duration.ofHours(8);

		Periodo periodo = new Periodo(cargaHoraria);

		System.out.println("\n\n");

		// System.out.println(periodo);

		periodo.addMarcacao(8, 0, 7, 9, 2024);

		periodo.addMarcacao(12, 0, 7, 9, 2024);

		periodo.addMarcacao(13, 0, 7, 9, 2024);

		periodo.addMarcacao(18, 0, 7, 9, 2024);

		periodo.ordenaPeriodo();

		System.out.println(periodo);

		System.out.println(periodo.calculaHorasTrabalhadas());
		System.out.println(periodo.getSaldo());
		System.out.println("Intervalo: " + periodo.calculaIntervalo());
	}

}
