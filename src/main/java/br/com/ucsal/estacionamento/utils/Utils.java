package br.com.ucsal.estacionamento.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    public static String formataMensagemSaida(String placa, LocalDateTime dataHrEntrada, LocalDateTime dataHrSaida, Double valorAPagar) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Duration duracao = Duration.between(dataHrEntrada, dataHrSaida);
        long horas = duracao.toHours();
        long minutos = duracao.toMinutesPart();
        long segundos = duracao.toSecondsPart();

        String dataEntradaFormatada = dataHrEntrada.format(formatoData);
        String dataSaidaFormatada = dataHrSaida.format(formatoData);

        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.getDefault());
        simbolos.setDecimalSeparator('.');
        DecimalFormat formatoValor = new DecimalFormat("0.00", simbolos);
        String valorFormatado = formatoValor.format(valorAPagar);

        return "Placa: " + placa + "\nEntrada: " + dataEntradaFormatada + "\nSaída: " + dataSaidaFormatada +
                "\nDuração: " + horas + "h " + minutos + "m " + segundos + "s" + "\nValor a Pagar: R$" + valorFormatado;
    }
}
