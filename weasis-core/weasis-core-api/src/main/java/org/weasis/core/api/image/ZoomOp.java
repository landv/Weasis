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

import java.awt.Dimension;
import java.awt.image.RenderedImage;

import org.opencv.imgproc.Imgproc;
import org.weasis.core.api.Messages;
import org.weasis.core.api.gui.util.MathUtil;
import org.weasis.core.api.image.cv.ImageProcessor;

public class ZoomOp extends AbstractOp {

    public static final String OP_NAME = Messages.getString("ZoomOperation.title"); //$NON-NLS-1$

    public static final String[] INTERPOLATIONS =
        { Messages.getString("ZoomOperation.nearest"), Messages.getString("ZoomOperation.bilinear"), //$NON-NLS-1$ //$NON-NLS-2$
            Messages.getString("ZoomOperation.bicubic"), Messages.getString("ZoomOperation.lanczos") }; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Set a zoom factor in x-axis (Required parameter).
     *
     * Double value.
     */
    public static final String P_RATIO_X = "ratio.x"; //$NON-NLS-1$

    /**
     * Set a zoom factor in y-axis (Required parameter).
     *
     * Double value.
     */
    public static final String P_RATIO_Y = "ratio.y"; //$NON-NLS-1$

    /**
     * Set the interpolation type (Optional parameter).
     *
     * Integer value. Default value is bilinear interpolation. See javax.media.jai.Interpolation.
     */
    public static final String P_INTERPOLATION = "interpolation"; //$NON-NLS-1$

    public ZoomOp() {
        setName(OP_NAME);
    }

    public ZoomOp(ZoomOp op) {
        super(op);
    }

    @Override
    public ZoomOp copy() {
        return new ZoomOp(this);
    }

    @Override
    public void process() throws Exception {
        RenderedImage source = (RenderedImage) params.get(Param.INPUT_IMG);
        RenderedImage result = source;
        Double zoomFactorX = (Double) params.get(P_RATIO_X);
        Double zoomFactorY = (Double) params.get(P_RATIO_Y);

        if (zoomFactorX != null && zoomFactorY != null
            && (MathUtil.isDifferent(zoomFactorX, 1.0) || MathUtil.isDifferent(zoomFactorY, 1.0))) {
            Dimension dim = new Dimension((int) (Math.abs(zoomFactorX) * source.getWidth()),
                (int) (Math.abs(zoomFactorY) * source.getHeight()));
            Integer interpolation = (Integer) params.get(P_INTERPOLATION);
            if (Math.abs(zoomFactorX) < 0.8) {
                interpolation = Imgproc.INTER_AREA;
            } else if (interpolation != null && interpolation == 3) {
                interpolation = 4;
            }
            result = ImageProcessor.scale(source, dim, interpolation);
        }

        params.put(Param.OUTPUT_IMG, result);
    }

}
