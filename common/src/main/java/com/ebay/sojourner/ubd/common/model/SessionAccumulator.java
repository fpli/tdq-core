package com.ebay.sojourner.ubd.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SessionAccumulator implements Serializable {

    private UbiSession ubiSession;

    public SessionAccumulator() {
        this.ubiSession = new UbiSession();
    }

}