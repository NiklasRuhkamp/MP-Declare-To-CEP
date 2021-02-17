package com.cor.cep.graphicalUserInterface;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.Color;

public class TableColorCellRenderer implements TableCellRenderer{

    private static final TableCellRenderer RENDERER = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable constraintMonitoring,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {

				Component c = RENDERER.getTableCellRendererComponent(constraintMonitoring, value,
						isSelected, hasFocus, row, column);

				if (value.equals("activation")) {
					c.setBackground(Color.YELLOW);
					c.setForeground(Color.YELLOW);
				} 
				else if (value == "fulfillment") {
					c.setBackground(Color.GREEN);
					c.setForeground(Color.GREEN);
				}
				else if (value == "violation") {
					c.setBackground(Color.RED);
					c.setForeground(Color.RED);
				}
				else {
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
                }
				return c;
			} 

}