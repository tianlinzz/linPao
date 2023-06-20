package com.tianlin.linpaobackend.model.domain.request;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;

@Data
public class UpdateSelf implements Serializable {

    private static final long serialVersionUID = 21412312L;

    private Long id;

    private String type;

    private String value;

}
