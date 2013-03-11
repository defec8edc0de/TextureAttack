/*******************************************************************************
 * Copyright (c) 2012 Sebastian Funke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Sebastian Funke - initial API and implementation
 ******************************************************************************/
package de.tud.textureAttack.view.components;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel statusLabel;
	private JProgressBar progressBar;
	private JButton chancelButton;
	private ActionController actionController;
	private AbstractAlgorithm algorithm;
	private String task;
	private Object taskResult;
	private JDialog modalDialog;
	private JProgressBar imageProgress;
	private boolean canceledAutoAttack;

	public StatusBar(String initStatus, ActionController actionController,
			int parentWidth) {
		this.actionController = actionController;
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(parentWidth, 16));
		setLayout(new BorderLayout());
		statusLabel = new JLabel(initStatus);
		statusLabel.setSize(new Dimension(parentWidth - 80, 20));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(statusLabel, BorderLayout.WEST);

		canceledAutoAttack = false;
		imageProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		imageProgress.setPreferredSize(new Dimension(150, 20));
		imageProgress.setValue(0);

		modalDialog = new JDialog();
		modalDialog.setTitle("Working..");
		modalDialog.setModal(true);
		modalDialog.add(imageProgress);
		modalDialog.setLocationRelativeTo(this);
		modalDialog.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				canceledAutoAttack = true;
				chancelButtonClick();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		modalDialog.pack();

		chancelButton = new JButton("Chancel");
		chancelButton.setEnabled(false);
		chancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StatusBar.this.chancelButtonClick();
			}

		});

		progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
		progressBar.setPreferredSize(new Dimension(150, 20));
		progressBar.setValue(0);

		JPanel progressPanel = new JPanel(new BorderLayout());
		progressPanel.add(progressBar, BorderLayout.WEST);
		progressPanel.add(chancelButton, BorderLayout.EAST);

		add(progressPanel, BorderLayout.EAST);
	}

	private void chancelButtonClick() {
		algorithm.cancel(true);
	}

	public void setStatus(String newStatus) {
		statusLabel.setText(newStatus);
	}

	public Object startTask(String task, AbstractAlgorithm algo) {
		this.task = task;
		setStatus(task);
		algorithm = algo;
		chancelButton.setEnabled(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		taskResult = null;

		algorithm.execute();
		if (!modalDialog.isVisible())
			modalDialog.setVisible(true);

		taskResult = algorithm.getResult();
		return taskResult;

	}

	public void setProgressCount(int i) {
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		if (i == 1)
			progressBar.setMaximum(i);
		else
			progressBar.setMaximum(i - 1);
	}

	public void setProgress(int i) {
		progressBar.setValue(i);
		String procentual = String.valueOf(Math.floor(progressBar
				.getPercentComplete() * 100)) + " %";
		progressBar.setString(procentual);
		if (progressBar.getPercentComplete() == 1.0) {
			progressBar.setStringPainted(false);
		}
		progressBar.paint(progressBar.getGraphics());

	}

	public int getProgress() {
		return progressBar.getValue();
	}

	public void doneAll() {
		modalDialog.setVisible(false);
		canceledAutoAttack = false;
		System.out.println("All Textures processed!");
		setStatus("All Textures processed!");
	}

	public boolean getCanceledAutoAttack() {
		return canceledAutoAttack;
	}

	public void done() {
		chancelButton.setEnabled(false);
		setCursor(null); // turn off the wait cursor
		modalDialog.setVisible(false);
		if (algorithm.isCancelled()) {
			System.out.println(task.toString() + " canceled!");
			setStatus(task.toString() + " canceled!");
		} else {
			System.out.println(task.toString() + " done!");
			setStatus(task.toString() + " done!");
		}
	}

	public void setImageProgressParameter(int max) {
		imageProgress.setStringPainted(true);
		imageProgress.setValue(0);
		imageProgress.setMaximum(max);
	}

	public void setImageProgress(int i) {
		imageProgress.setValue(i);
		imageProgress.setString(i + "/" + imageProgress.getMaximum()
				+ " processing...");
		imageProgress.paint(imageProgress.getGraphics());
	}

	public void resetAlgorithms() {
		actionController.resetAlgorithms();
	}

}
