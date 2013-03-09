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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import de.tud.textureAttack.controller.ActionController;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel statusLabel;
	private JProgressBar progressBar;
	private JButton chancelButton;
	private ActionController actionController;
	private Enum task;


	public StatusBar(String initStatus, ActionController actionController, int parentWidth) {
		this.actionController = actionController;
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(parentWidth, 16));
		setLayout(new BorderLayout());
		statusLabel = new JLabel(initStatus);
		statusLabel.setSize(new Dimension(parentWidth - 80, 20));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(statusLabel, BorderLayout.WEST);

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
		// TODO Auto-generated method stub	
	}
	
	public void setStatus(String newStatus) {
		statusLabel.setText(newStatus);
	}
	
	public void startTask(Enum task){
		this.task = task;
		chancelButton.setEnabled(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
	
	public int getProgress(){
		return progressBar.getValue();
	}
	
	public void done(Enum taskName){
		chancelButton.setEnabled(false);
		setCursor(null); // turn off the wait cursor
		System.out.println(taskName.toString()+" done!");
		setStatus(taskName.toString()+" done!");
	}
	
	
	
	

		
		
//		/*
//		 * Main task. Executed in background thread.
//		 */
//		@Override
//		public Void doInBackground() {
//			int progress = 0;
//			// Initialize progress property.
//			setProgress(0);
//			while (progress < 100) {
//				// Sleep for up to one second.
//					
//				// TODO
//					
//				setProgress(Math.min(progress, 100));
//			}
//			return null;
//		}

		/*
		 * Executed in event dispatching thread
		 */
	
	
	


}
