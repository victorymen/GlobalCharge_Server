package org.appserver.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class CardsStock extends Model<CardsStock> {

    private String cardpassword;

    private Integer sellpice;

    private String starttime;

    private Integer isused;

    private Long id;
}
