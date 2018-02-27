package com.littlersmall.lightdao.creator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import lombok.Data;

/**
 * Created by littlersmall on 2017/6/9.
 */
@Data
public class ConditionModel {
    private List<String> conditionList = new ArrayList<>();
    private MapSqlParameterSource parameterSource = new MapSqlParameterSource();
}
