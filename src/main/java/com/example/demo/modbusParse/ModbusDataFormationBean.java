package com.example.demo.modbusParse;


import lombok.Data;

@Data
public class ModbusDataFormationBean {
    private Integer addr;//地址
    private Integer funcode;//功能码
    private Integer portNumber;//寄存器起始地址
    private Integer dataType;//1代表16位int，2代表32位Double
    private Integer value;//值

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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }


    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
