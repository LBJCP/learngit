package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * voice_note
 * @author 
 */
@Data
public class VoiceNote implements Serializable {
    private Integer id;

    private String userid;

    private String note;

    private Long time;

    private static final long serialVersionUID = 1L;
}