package com.rea_lity.modle;

import com.rea_lity.modle.enums.ImageCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResource implements Serializable {
    /**
     * 图片资源类型
     */
    ImageCategoryEnum imageCategory;

    /**
     * 图片名称
     */
    String imageName;

    /**
     * 图片描述
     */
    String imageDescription;

    /**
     * 图片链接
     */
    String imageUrl;
}
