import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class Buju implements ActionListener{
	
	String filePath = new String();
	String result = new String();
	double min_support = 0;
	
    JFrame frame = new JFrame("Apriori Algorithm");
    JTabbedPane tabPane = new JTabbedPane();
    Container con = new Container();
    Container con1 = new Container();
    JLabel label1 = new JLabel("Open File");
    JLabel label2 = new JLabel("Min Support (0~1)");
    JLabel label3 = new JLabel("Frequent itemsets");
    JLabel label4 = new JLabel("Copyright Guangyao Chen, 2016.");
    JLabel label5 = new JLabel("All rights reserved.");
    JTextField text1 = new JTextField();
    JTextField text2 = new JTextField();
    JTextArea text3 = new JTextArea();
    JScrollPane text4 = new JScrollPane(text3);
    JButton button1 = new JButton("Run");
    JButton button2 = new JButton("...");
    JButton button3 = new JButton("Save Result");
    JFileChooser jfc = new JFileChooser();
    JFileChooser  jsv = new JFileChooser();
    
    Buju(){
        jfc.setCurrentDirectory(new File("c:\\"));

        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int)(lx/2)-150,(int)(ly/2)-150));
        frame.setSize(400,500);
        frame.setContentPane(tabPane);

        label1.setBounds(10,30,100,20);
        label2.setBounds(10,70,100,20);
        label3.setBounds(10,120,150,20);
        label4.setBounds(100,100,200,200);
        label5.setBounds(140,130,200,200);

        text1.setBounds(130,30,200,20);
        text2.setBounds(130,70,200,20);
        //text3.setSize(350,250);
        //text3.setBounds(10,150,350,250);
        //text3.setLineWrap(true); 
        text4.setSize(350,250);
        text4.setBounds(10,150,350,250);
        
        text4.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
        text4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        
        text3.setTabSize(1);
        text1.setFont(new Font("Consolas", Font.BOLD, 12));
        text2.setFont(new Font("Consolas", Font.BOLD, 12));
        text3.setFont(new Font("Consolas", Font.BOLD, 12));

        button1.setBounds(130,110,60,30);
        button2.setBounds(330,30,30,20);
        button3.setBounds(210,110,120,30);
        
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);

        con1.add(label4);
        con1.add(label5);
        con.add(label1);
        con.add(label2);
        con.add(label3);
        con.add(text1);
        con.add(text2);
        //con.add(text3);
        con.add(text4);
        con.add(button1);
        con.add(button2);
        con.add(button3);
        con.add(jfc);
        tabPane.add("Apriori Algorithm", con);
        tabPane.add("About", con1);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e) {
    	
        if (e.getSource().equals(button1)) {
        	min_support = Double.parseDouble(text2.getText());
        	
        	if (min_support < 0 || min_support > 1) {
        		JOptionPane.showMessageDialog(null, "Min Support must between 0 to 1!");
        		return;
        	}
        	
        	result = Apriori.AprioriAlgorithm(min_support, filePath);
        	JOptionPane.showMessageDialog(null, "Finish!");
        	text3.setText(result);
        }
        
        if (e.getSource().equals(button2)) {
            jfc.setFileSelectionMode(0);
            int state = jfc.showOpenDialog(null);
            if (state == 1) {
                return; 
            }
            else{
                File f = jfc.getSelectedFile();
                text1.setText(f.getAbsolutePath());
                filePath = f.getAbsolutePath();
            }
        }
        
        if (e.getSource().equals(button3)) {
        	File file = null;
        	String fileName = null;
        	BufferedWriter bw = null;
        	
        	if (jsv.showSaveDialog(button3) == jfc.APPROVE_OPTION) {
        		file = jsv.getSelectedFile();
        	}
        	
        	fileName = jsv.getName(file);
        	
            if(fileName==null|| fileName.trim().length()==0){
                JOptionPane.showMessageDialog(null, "The file's name is empty.");
            }
        	
            if(file.isFile()){
                fileName = file.getName();
            }
            
            file = jsv.getCurrentDirectory();
            
            String path = file.getPath() + "/" + fileName;
            file =new File(path);
        
            if (file.exists()) { 
                 int i = JOptionPane.showConfirmDialog(null, "The file already exists, sure you want to cover?");     
                 if(i == JOptionPane.YES_OPTION)   ;     
                 else   return ;    
            } 
            
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                bw.write(result);
                bw.flush();
            } 
            catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(null, "Error" + e1.getMessage());
            } 
            catch (IOException e1) {
                e1.printStackTrace();
            }
            finally{
                try {
                    if(bw!=null) bw.close();
                } 
                catch (IOException e1) {
                }
            }
        }
    }

	public static void main(String[] args) {
        new Buju();
    }
}