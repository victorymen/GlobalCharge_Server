package org.appserver.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CardStock {
    private BigInteger id;
    private String cardpassword;
    private int sellpice;
    private String starttime;
    private boolean isused;
}
