/*******************************************************************************
 * Copyright (c) 2016 Weasis Team and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 *******************************************************************************/
package org.weasis.core.api.image;

import java.awt.image.RenderedImage;
import java.util.Optional;

import org.opencv.core.Core;
import org.weasis.core.api.Messages;
import org.weasis.core.api.image.cv.ImageProcessor;

public class RotationOp extends AbstractOp {

    public static final String OP_NAME = Messages.getString("RotationOperation.title"); //$NON-NLS-1$

    /**
     * Set the clockwise angle value in degree (Required parameter).
     *
     * Integer value.
     */
    public static final String P_ROTATE = "rotate"; //$NON-NLS-1$

    public RotationOp() {
        setName(OP_NAME);
    }

    public RotationOp(RotationOp op) {
        super(op);
    }

    @Override
    public RotationOp copy() {
        return new RotationOp(this);
    }

    @Override
    public void process() throws Exception {
        RenderedImage source = (RenderedImage) params.get(Param.INPUT_IMG);
        RenderedImage result = source;
        Integer rotationAngle = Optional.ofNullable((Integer) params.get(P_ROTATE)).orElse(0);
        rotationAngle = rotationAngle % 360;

        if (rotationAngle != 0) {
            // optimize rotation by right angles
            Integer rotOp = null;
            if (rotationAngle == 90) {
                rotOp = Core.ROTATE_90_CLOCKWISE;
            } else if (rotationAngle == 180) {
                rotOp = Core.ROTATE_180;
            } else if (rotationAngle == 270) {
                rotOp = Core.ROTATE_90_COUNTERCLOCKWISE;
            }
            if (rotOp != null) {
                result = ImageProcessor.getRotatedImage(source, rotOp);
            } else {
                result = ImageProcessor.getRotatedImage(source, rotationAngle, source.getWidth() / 2.0,
                    source.getHeight() / 2.0);
            }
        }
        params.put(Param.OUTPUT_IMG, result);
    }

}
