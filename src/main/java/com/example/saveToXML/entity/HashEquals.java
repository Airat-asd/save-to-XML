package com.example.saveToXML.entity;

import java.util.Objects;

public class HashEquals {

    public boolean equalsDIY(Department first, Object second) {
        if (first == second) return true;
        if (second == null || getClass() != second.getClass()) return false;
        Department that = (Department) second;
        return Objects.equals(first.getDepCode(), that.getDepCode()) &&
                Objects.equals(first.getDepJob(), that.getDepJob()) &&
                Objects.equals(first.getDescription(), that.getDescription());
    }

    public int hashCodeDIY(Department object) {
        return Objects.hash(object.getDepCode(), object.getDepJob());
    }
}
