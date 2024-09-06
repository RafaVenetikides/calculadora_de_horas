package rafael.venetikides.calc_backend;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import rafael.venetikides.calc_backend.classes.Horario;
import rafael.venetikides.calc_backend.classes.Periodo;

@SpringBootApplication
public class CalcBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalcBackendApplication.class, args);

		LocalTime cargaHoraria = LocalTime.of(8, 0);

		Periodo periodo = new Periodo(cargaHoraria);

		System.out.println("\n\n");

		// System.out.println(periodo);

		periodo.ordenaPeriodo();

		System.out.println(periodo);

		// System.out.println(periodo.calculaHorasTrabalhadas());
	}

}
