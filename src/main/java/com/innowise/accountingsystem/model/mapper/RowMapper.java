package com.innowise.accountingsystem.model.mapper;

import java.sql.ResultSet;
import java.util.Optional;

public interface RowMapper<T> {

    Optional<T> mapRow(ResultSet resultSet);

}
