package br.com.bibliotech.diadema.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmprestimoResponse(
        Long id,
        LocalDateTime dataSaida,
        LocalDateTime dataDevolucao
)
{
    public EmprestimoResponse(br.com.bibliotech.diadema.model.Emprestimos emp){
            this(emp.getId(), emp.getDataSaida(), emp.getDataDevolucao());
    }
}
