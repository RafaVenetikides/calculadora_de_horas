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

		LocalTime h1 = LocalTime.of(12,00);
		LocalDate d1 = LocalDate.of(2024, 9, 04);

		Horario hour1 = new Horario(h1, d1);

		periodo.addMarcacao(hour1);

		LocalTime h2 = LocalTime.of(8,00);
		LocalDate d2 = LocalDate.of(2024, 9, 04);

		Horario hour2 = new Horario(h2, d2);

		periodo.addMarcacao(hour2);

		System.out.println(periodo);

		periodo.ordenaPeriodo();

		System.out.println(periodo);

		System.out.println(periodo.calculaHorasTrabalhadas());
	}

}
