package com.example.demo.modbusParse;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ModbusDataAnalyzeBean {
    private Integer addr;//地址
    private Integer funcode;//功能码
    private Integer dataType;//1代表16位int，2代表32位Double
    private ArrayList<Double> values;//寄存器值

    public Integer getAddr() {
        return addr;
    }

    public void setAddr(Integer addr) {
        this.addr = addr;
    }

    public Integer getFuncode() {
        return funcode;
    }

    public void setFuncode(Integer funcode) {
        this.funcode = funcode;
    }


    public ArrayList<Double> getValues() {
        return values;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}

