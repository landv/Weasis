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
package org.weasis.core.api.image.op;

import java.awt.image.RenderedImage;
import java.util.List;

import org.weasis.core.api.Messages;
import org.weasis.core.api.gui.task.TaskInterruptionException;
import org.weasis.core.api.gui.task.TaskMonitor;
import org.weasis.core.api.gui.util.GuiExecutor;
import org.weasis.core.api.image.cv.ImageProcessor;
import org.weasis.core.api.media.data.ImageElement;
import org.weasis.core.api.util.StringUtil;

public class MeanCollectionZprojection {

    private final List<ImageElement> sources;
    private final TaskMonitor taskMonitor;

    public MeanCollectionZprojection(List<ImageElement> sources, TaskMonitor taskMonitor) {
        if (sources == null) {
            throw new IllegalArgumentException("Sources cannot be null!"); //$NON-NLS-1$
        }
        this.sources = sources;
        this.taskMonitor = taskMonitor;
    }

    private void incrementProgressBar(final int progress) {
        if (taskMonitor == null) {
            return;
        }
        if (taskMonitor.isCanceled()) {
            throw new TaskInterruptionException("Operation from " + this.getClass().getName() + " has been canceled"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (taskMonitor.isShowProgression()) {
            GuiExecutor.instance().execute(new Runnable() {

                @Override
                public void run() {
                    taskMonitor.setProgress(progress);
                    StringBuilder buf = new StringBuilder(Messages.getString("MeanCollectionZprojection.operation")); //$NON-NLS-1$
                    buf.append(StringUtil.COLON_AND_SPACE);
                    buf.append(progress);
                    buf.append("/"); //$NON-NLS-1$
                    buf.append(taskMonitor.getMaximum());
                    taskMonitor.setNote(buf.toString());
                }
            });
        }
    }

    public RenderedImage computeMeanCollectionOpImage() {
          return ImageProcessor.meanStack(sources);
    }
}
