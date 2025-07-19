package com.aluracursos.javachallenge;

public record ConversionExchangeRateApi(String base_code,
                                        String target_code,
                                        double conversion_rate,
                                        String result,
                                        String errorType) {
}
