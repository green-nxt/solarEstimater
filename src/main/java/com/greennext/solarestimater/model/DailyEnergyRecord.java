package com.greennext.solarestimater.model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DailyEnergyRecord {

        private String id;
        private String timestamp;
        private String manufacturersName;
        private String sn;
        private String equipmentType1;  // There are two "Equipment type" fields
        private String equipmentType2;
        private String securityType;
        private String customStandard;
        private String outputPower;      // Value with unit W
        private String outputS;          // Value with unit VA
        private String outputQ;          // Value with unit VA
        private String outputPF;
        private String instrumentPower;  // Value with unit W
        private String energyToday;      // Value with unit kWh
        private String energyTotal;      // Value with unit kWh
        private String cumulativeTime;   // Value with unit h
        private String inverterStatus;
        private String waitingTime;      // Value with unit S
        private String pv1Voltage;       // Value with unit V
        private String pv1Current;       // Value with unit A
        private String pv2Voltage;       // Value with unit V
        private String pv2Current;       // Value with unit A
        private String pv3Voltage;       // Value with unit V
        private String pv3Current;       // Value with unit A
        private String gridRVoltage;     // Value with unit V
        private String gridRCurrent;     // Value with unit A
        private String gridSVoltage;     // Value with unit V
        private String gridSCurrent;     // Value with unit A
        private String gridTVoltage;     // Value with unit V
        private String gridTCurrent;     // Value with unit A
        private String gridLineVoltageRS;// Value with unit V
        private String gridLineVoltageST;// Value with unit V
        private String gridLineVoltageTR;// Value with unit V
        private String gridFrequency;    // Value with unit HZ
        private String busVoltage;       // Value with unit V
        private String iso;
        private String dci;
        private String gfci;
        private String cuf;              // Value with unit %
        private String pr;               // Value with unit %
}
