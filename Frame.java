// file: EmptyFrame1.java
import javax.sound.sampled.*;
	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.event.*;
	import javax.swing.table.*;
	import javax.swing.*;
	import javax.swing.filechooser.*;
	import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.commons.lang3.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.net.*;
import java.net.URL;
import java.net.URI;
import java.io.BufferedReader;
import java.io.*;


class CustomComparator implements Comparator<SongData> {
    
    public int compare(SongData o1, SongData o2) {
        return o1.compareTo(o2);
    }
}

class SongData implements Comparable, Serializable
{
	public String song;
	public String artist;
	public ArrayList<Integer> profanity;
	public int success;
	public int id;
	public String URL;
	SongData(String s,String a,ArrayList<Integer>p, int suc, int _id)
	{
	success = suc;
	song = s;
	artist = a;
	profanity = p;
	id = _id;
	
	}
		SongData(String s,String a,ArrayList<Integer>p, int suc, int _id, String U)
	{
	success = suc;
	song = s;
	artist = a;
	profanity = p;
	id = _id;
	URL = U;
	}
	public int getSuccess()
	{
	return success;
	}
	public int composite()
	{
	int result = 0;
		for ( int i = 0; i < profanity.size(); i++ )
		{
		result += profanity.get(i);
		}
	return result;
	}
	public int compareTo(Object o)
		{
			SongData a = (SongData)o;
			if ( this.success == -1 && a.getSuccess() != -1 )
			{
				// lets put those who didn't succeed at the end, because noone cares about them
				return 1;
	
			}	
			if ( this.success == -1 && a.getSuccess() == 1 )
			{
				return 1;
			}

			if ( this.success == 1 && a.getSuccess() == -1 )
			{
				return -1;
			}

			if ( this.success == 1 && a.getSuccess() == 1 )
			{
				// sort by number of curses
						
						return ((this.composite() > a.composite()) ? 1 : -1 );
						
				
			
			}
			
			return -1;
			// implement comparable interface
		
		
		
		}


}

class ModelT extends AbstractTableModel implements TableModel
{
	private EmptyFrame1 delegate;
	ModelT(EmptyFrame1 d)
	{
	super();
	delegate = d;
	}
	 public int getColumnCount() {
        return 3 + delegate.profanity.size();
    }

    public int getRowCount() {
        return delegate.songs.size();
    }

    public String getColumnName(int col) {
        switch ( col )
        {
         	
        case 0:
        	return "Artist";
        	
        case 1:
        	return "Song";
        	
        case 2:
        	return "Total Profanity";
        }
        
        return delegate.profanity.get(col-3);
        
       
    }

    public Object getValueAt(int row, int col) {
        SongData d = delegate.songs.get(row);
        if ( col == 0 )
        {
        return d.artist;
        }
        if ( col == 1 )
        {
        return d.song;
        }
        if ( col == 2 )
        {
        if ( d.success == -1 )
        {
        return "No Lyrics";
        }
        if ( d.profanity == null )
        {
        d.profanity = new ArrayList<Integer>();
        }
        
        
        if ( d.profanity.size() < delegate.profanity.size() )
        {
        // d's profanity is invalid at the moment
        for ( int i = 0; i < delegate.profanity.size(); i++ )
        {
        d.profanity.add(0);
        }
        
        }
        
        int total = 0;
        for ( int i = 0; i < d.profanity.size(); i++ )
        {
        total += (d.profanity.get(i) != null) ? d.profanity.get(i) : 0;
        } 
         // composite profanity rating:
       Integer i = new Integer(total);
       return i;
        
        
        }
 		
 		// load individual profanity
 		int number = 0;
 		try {
 		number = d.profanity.get(col-3);
 		}
 		catch ( java.lang.IndexOutOfBoundsException b )
 		{
 		number = 0;
 		
 		}
 		Integer p = new Integer(number);
 		
 		return p;
 		
 	}

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }



}
class Searcher implements Runnable
{
public EmptyFrame1 parent;
public String search;
public void run()
{
	parent.display.clear();
	for ( int i = 0; i < parent.songs.size(); i++ )
	{
	SongData d = parent.songs.get(i);
	
	if ( d.song.indexOf(search) == 0 )
	{
		parent.display.add(d);
	
	}
	
	
	
	
	}
	// now that we've searched...
	parent.modelT.fireTableDataChanged();
	

}


}

class DMan implements ActionListener {

 public EmptyFrame1 parent;
 
 DMan(EmptyFrame1 d)
 {
 this.parent = d;
 }
 @Override
 public void actionPerformed(ActionEvent evt) {

	//System.out.println("pressed");
	
	if ( parent.profanity == null || parent.profanity.size() == 0)
	{
	JOptionPane.showMessageDialog(null, "Please make sure to select a text file, with one profane word per line! If you're confused, consult help.","Error", JOptionPane.ERROR_MESSAGE);
	return;
	}
	
	if ( parent.runningProgram == 1 )
	{
	
	JOptionPane.showMessageDialog(null, "The engine is already running. Please wait for the current action to be completed before requesting another. Thank you.","Error", JOptionPane.ERROR_MESSAGE);
	return;
	
	}
	
  if ( parent.runningProgram == 0)
  {
  
  /////////  PROGRAM INIT()
  
  
   parent.parser = new SAXParserExample();
    parent.parser.LIBRARY_FILE_PATH = parent.textField.getText();
    parent.parser.parseDocument();
     parent.bar.setMinimum(0);
    parent.bar.setMaximum(parent.parser.myTracks.size());
     int NUMDONE = 0;
        parent.totalCount = parent.parser.myTracks.size();
        
  	parent.bar.setMaximum(parent.totalCount);
  	if ( parent.remainingLabel == null )
  	{
  	parent.remainingLabel = new JLabel("Time Remaining:");
  	parent.remainingLabel.setBounds(770,280,150,60);
  //	parent.getContentPane().add(parent.remainingLabel);
  	
  	}
  	parent.remainingLabel.setText("Time Remaining: ");
  	if ( parent.percentageLabel == null )
  	{
  	parent.percentageLabel = new JLabel("0%");
    parent.percentageLabel.setBounds(820,260,60,80);
    parent.getContentPane().add(parent.percentageLabel);
    }
    parent.percentageLabel.setText("0%");
    parent.percentageProgress = 0;
   if ( parent.songs != null)
   {
  	parent.songs.clear();
  	}
  	else
  	{
  	parent.songs = new ArrayList<SongData>();
  	}
  	//modelT.fireTableDataChanged();
	
	parent.fatherTime = new Timer(1000,parent);
	parent.deltaT = 0;
	parent.completedSongs = 0;
	DownloadManager a = new DownloadManager();
	
	
	
	a.parent = parent;
	System.out.println("starting...");
	//parent.fatherTime.start();
	new Thread(a).start();
	parent.runningProgram = 1;
}
    






}



}


class LyricsFrame extends JFrame implements ActionListener
{
	public String artist;
	public String song;
	public String URL;
	public JEditorPane browser;
	public EmptyFrame1 ancestor;
	 public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
 
 	}
	
	public void setupBrowser()
	{
		try 
			{
		
		URL	url =  new URL(URL);
		
		
			URLConnection connection = url.openConnection(); 

			BufferedReader rd;

			InputStream stream = connection.getInputStream();

			rd = new BufferedReader(new InputStreamReader(stream));

			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null)
			{
 				   sb.append(line);
			}
			rd.close();

			String resultedString = sb.toString();
			String dump = resultedString;
			
			String marker = "height='17'/></a></div>";
			String endMarker = "<!--";
    	
			String firstHalf = dump.substring(0,dump.indexOf(marker) + marker.length());
			
			int index = dump.indexOf(marker) + marker.length();
			
			String lyrics = dump.substring(index,dump.indexOf(endMarker,index+1));
			
			lyrics = StringEscapeUtils.unescapeHtml4(lyrics);
			
			String secondHalf = dump.substring(index + lyrics.length());
			
			
			
			
			
		
		
			for ( int i =0; i < ancestor.profanity.size(); i++)
			{
			// let's play some funky music, whiteboy.
			
			String highlight = "<SPAN style=\"BACKGROUND-COLOR: #ffff00\">" + ancestor.profanity.get(i) + "</SPAN>";
			
    	
    		lyrics = lyrics.replaceAll("(?i)" + ancestor.profanity.get(i),highlight);
			}
			
			String compiled = firstHalf + lyrics + secondHalf;
			
			System.out.println("--------");
			System.out.println(compiled);
			// this should do the job..
			 JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
      	
      		browser = new JEditorPane();
			browser.setContentType("text/html");
			lyrics = "<center>" + lyrics + "</center>";
			browser.setText(lyrics);
		jScrollPane1.setViewportView(browser);
			jScrollPane1.setBounds(0,0,600,600);
			getContentPane().add(jScrollPane1);
			browser.setEditable(false);
			try {
			//browser.setText(compiled);
			}
			catch ( Exception e )
			{
			System.out.println("loading error");
			}
			
			
		}
		catch ( java.net.MalformedURLException e )
			{
				return;
			}
		catch( java.io.IOException d )
			{
				return;
			}	
	
	}
	


 	
 	
 	public String replace(String str, String pattern, String replace) {
    int s = 0;
    int e = 0;
    
    StringBuffer result = new StringBuffer();

    while ((e = str.indexOf(pattern, s)) >= 0) {
        result.append(str.substring(s, e));
        result.append(replace);
        s = e+pattern.length();
    }
    result.append(str.substring(s));
    return result.toString();
}

 	
 

 	LyricsFrame(String _URL,String _artist,String _song, EmptyFrame1 d)
 	{
 	ancestor = d;
 	artist = _artist;
 	song   = _song;
 	URL    = _URL;
 	
	setLayout(null);
	setTitle(artist + " - " + song + " Lyrics");
	setSize(600,800); // default size is 0,0
	setLocation(200,200); // default is 0,0 (top left corner)
	setVisible(true);
	setupBrowser();
	
	}






}






class EmptyFrame1 extends JFrame implements ActionListener, DocumentListener, MouseListener {

  // Constructor:
  public JMenuItem _save;
  public JMenuItem _load;
  public JMenuItem _help;
  public JMenuItem _exit;
  public JMenuBar menuBar;
  public JMenu filemenu;
  public JMenu helpmenu;
  public ArrayList<String> profanity;
  public JButton chooseIndex;
  public JTextField textField;
  public JTextField profField;
  public JTextField searchField;
  public JTable profTable;
  public JLabel percentageLabel;
  public int percentageProgress;
  public JButton searchButton;
  public JButton chooseProf;
  public JButton goButton;
  public JProgressBar bar;
  public ModelT modelT;
  public int runningProgram;
  public  SAXParserExample parser;
  public DefaultListModel model;
  static final int _LEFT = 10;
  int totalCount;
  public ArrayList<SongData> songs;
  public ArrayList<SongData> display;
 public TableRowSorter<ModelT> sorter;
  public Timer fatherTime;
  public int completedSongs;
  public int deltaT;
  public JLabel remainingLabel;	
  public int ran;
	
private void search()
{
// find text that matches searchField
	// searchField.getText();
	
	Searcher s = new Searcher();
	s.parent = this;
	s.search = searchField.getText();
	new Thread(s).start();
	


}

  
  public EmptyFrame1() {
  addWindowListener(new WindowAdapter() {
	  	public void windowClosing(WindowEvent e) {
		   System.exit(0);
	  	} //windowClosing
	} );
	runningProgram = 0;
	songs = new ArrayList<SongData>();
	profanity = new ArrayList<String>();
    setLayout(null);
	setTitle("Explicitor");
	setSize(1114,500); // default size is 0,0
	setLocation(10,200); // default is 0,0 (top left corner)
	ran = 0;
	JLabel sT = new JLabel("Enter an Artist/Song to Search");
	sT.setBounds(664,320,290,25);
	getContentPane().add(sT);
	
//	searchButton = new JButton("Go");
//	searchButton.addActionListener(this);
//	searchButton.setBounds(1021,350,80,25);
	//getContentPane().add(searchButton);
	
	searchField = new JTextField();
	searchField.setBounds(660,350,300,25);
	searchField.getDocument().addDocumentListener(this);
getContentPane().add(searchField);

   goButton = new JButton("Create Cache");
    goButton.addActionListener(new DMan(this));
    goButton.setBounds(662,140,351,70);
    getContentPane().add(goButton);
	
	JLabel title = new JLabel("Explicitor: Scan Music For Profanity");
	title.setBounds(453,499-472,250,17);
    getContentPane().add(title);
	
	
	
	JLabel tName = new JLabel("iTunes Index File:");
	tName.setBounds(_LEFT + 2, 419,140,30);
	  getContentPane().add(tName);
	
	textField = new JTextField();
	textField.setBounds(224,421,789,22);
    getContentPane().add(textField);
    
	String loadPath = "";
	
	
	loadPath = System.getProperty("user.home") + "/Music/iTunes/iTunes Music Library.xml";
	textField.setText(loadPath);
    
	
	chooseIndex = new JButton("...");
	chooseIndex.addActionListener(this);
	chooseIndex.setBounds(1021,422,80,25);
    getContentPane().add(chooseIndex);
    
    modelT = new ModelT(this);
    profTable = new JTable(modelT);
    profTable.setBounds(10,60,630,340);
    sorter = new TableRowSorter<ModelT>(modelT);
    profTable.setRowSorter(sorter);
   JScrollPane p = new JScrollPane(profTable);
    p.setBounds(10,60,630,340);
   getContentPane().add(p);
    profTable.addMouseListener(this);
         
    
    JLabel loadProf = new JLabel("Load Profanity From File..");
    loadProf.setBounds(664,75,107,17);
    getContentPane().add(loadProf);
         
         profField = new JTextField();
    profField.setBounds(662,100,351,22);
   // profField.setEditable(false);
    getContentPane().add(profField);
    
    chooseProf = new JButton("...");
    chooseProf.addActionListener(this);
    chooseProf.setBounds(1021,100,80,25);
    getContentPane().add(chooseProf);
    
     
    JLabel a = new JLabel("Songs");
    a.setBounds(10,25,70,40);
    getContentPane().add(a);
    
    bar = new JProgressBar(0,1);
    bar.setBounds(662,240,351,40);
    getContentPane().add(bar);
    
    
    // set up the menu bar ------------
    
      // Constructor:
      if (System.getProperty("os.name").contains("Mac")) {
  System.setProperty("apple.laf.useScreenMenuBar", "true");
			}
      
 menuBar = new JMenuBar();
 filemenu = new JMenu("File");
 
 _save = new JMenuItem("Save as Archive...", KeyEvent.VK_S);
 _save.addActionListener(this);
 
 _exit = new JMenuItem("Exit");
 _exit.addActionListener(this);
 
 _load = new JMenuItem("Load archive...");
 _load.addActionListener(this);
 
 filemenu.add(_load);
 filemenu.add(_save);
 
 filemenu.addSeparator();
 
 filemenu.add(_exit);
 
 
  helpmenu = new JMenu("Help");
  
  _help = new JMenuItem("About Explicitor");
  _help.addActionListener(this);
  
  menuBar.add(filemenu);
  menuBar.add(helpmenu);
    
    
    
    setJMenuBar(menuBar);
  }
  
  
   
  
  
  
  
  
  
  
  public synchronized void got(SongData a)
  {
  	if ( songs == null )
  	{
  	songs = new ArrayList<SongData>();
  	}
  	
  	
  	// now, let's add our newly-found song
  	songs.add(a);
  	completedSongs++;
  	if ( songs.size() == totalCount )
  	{
  	success();
  
  	}
  	System.out.println("(" + songs.size() + " / " + totalCount + ")");  
  	bar.setValue(songs.size());
  	
  	percentageProgress =   (int) ( ( (float)songs.size()  /  (float)totalCount) * (float)100.0);
  	percentageLabel.setText(percentageProgress + "%");  
  	
  }
  
  
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
         JTable target = (JTable)e.getSource();
        
         
         int row = target.convertRowIndexToModel(target.getSelectedRow());
         
         SongData d = songs.get(row);
         
         if ( d.success == -1 )
         {
         JOptionPane.showMessageDialog(null, "Song does not have any assosciated lyrics.","Error", JOptionPane.ERROR_MESSAGE);
         return;
         }
         
         // now we're in the clear
         
         if ( d.URL == null )
         {
         JOptionPane.showMessageDialog(null, "Song does not have any assosciated lyrics.","Error", JOptionPane.ERROR_MESSAGE);
         return;
         }
         
         LyricsFrame l = new LyricsFrame(d.URL,d.artist,d.song,this);
    
         l.setVisible(true);
         
         
         
         // let's just print it out for now
         // this math works!
         
         // do some action
         }
   }

  
  public void mouseExited(java.awt.event.MouseEvent e)
  {
  
  return;
  }
  
  public void mouseEntered(java.awt.event.MouseEvent b)
  {
  return;
  }
  public void mouseReleased(java.awt.event.MouseEvent e)
  {
  return;
  }
  public void mousePressed(java.awt.event.MouseEvent e){
  return;
  }
  
   public void success()
  {
  try {
  completedSongs = -1;
  	ran = 1;
  		bar.setValue(0);
  	runningProgram = 0;
	URL url = this.getClass().getClassLoader().getResource("success.wav");
   
	 AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
         // Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.start();
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   //   Collections.sort(songs,new CustomComparator());
      

 modelT.fireTableStructureChanged();
 
profTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
profTable.getColumnModel().getColumn(0).setPreferredWidth(120);
profTable.getColumnModel().getColumn(1).setPreferredWidth(120);
profTable.getColumnModel().getColumn(2).setPreferredWidth(70);

// 310 + (profanity.size - 1) * 70;
for ( int i = 0; i < profanity.size(); i++)
{
profTable.getColumnModel().getColumn(i+3).setPreferredWidth(70);
}

int leftover = 630 - 310;
// thats how much is left over after we do the first three columns

if ( (leftover + (profanity.size() * 70) ) < 630 )
{

int dif = (630 - leftover) - ((profanity.size() - 1) * 70);

profTable.getColumnModel().getColumn(2+profanity.size()).setPreferredWidth(dif+6);



}




// modelT.fireTableDataChanged();




// now that we've succeeded, let's put this all into our table thing.
 
  
  }
  
   private void searchFieldChangedUpdate(DocumentEvent evt) {
  
  if ( ran == 1){
  String text = searchField.getText();
       if (text.length() == 0) {
            sorter.setRowFilter(null);
         } else {
             try {
                 sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
             } catch (Exception pse) {
                 JOptionPane.showMessageDialog(null, "Bad regex pattern", 
                     "Bad regex pattern", JOptionPane.ERROR_MESSAGE);
             }
         }
    }
    
    return;

 }
    public void insertUpdate(DocumentEvent evt) {
          searchFieldChangedUpdate(evt);
     }

   
     public void removeUpdate(DocumentEvent evt) {
           searchFieldChangedUpdate(evt);
     }

    
     public void changedUpdate(DocumentEvent evt) {
          searchFieldChangedUpdate(evt);
     }
     
  public void setGuessedTime(int guess)
  {
  
  // remainingLabel
  
  	int seconds;
  	int minutes;
  	int hours;
  	
  	// guess is in seconds...
  	
  	seconds = guess % 60;
  	
  	minutes = (int)(guess / 60 );
  	
  	if ( minutes > 59 )
  	{
  	hours = minutes / 60;
  	minutes = minutes - (hours * 60);
  	remainingLabel.setText(String.format("%2d:%2d:%2d",hours,minutes,seconds));
  	}
  	else
  	{
  	remainingLabel.setText(String.format("%2d:%2d",minutes,seconds));
  	}
  	
  	
  	
  	
  	
  
  
  
  
  }
  
  
  public String getExtension(File f)
{
String ext = null;
String s = f.getName();
int i = s.lastIndexOf('.');

if (i > 0 && i < s.length() - 1)
ext = s.substring(i+1).toLowerCase();

if(ext == null)
return "";
return ext;
}
  
  public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
    
    if ( src == _exit )
    {
    
    System.exit(0);
    
    }
    
    if ( src == _load )
    {
    
    	if ( runningProgram == 0 )
   		 {
    // let's load!
    
      JFileChooser chooser = new JFileChooser();
    		FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Explicitor Song Archive", "explic");
    		chooser.setFileFilter(filter);
  		    int returnVal = chooser.showOpenDialog(this);
    	if ( returnVal == JFileChooser.APPROVE_OPTION )
    		{
    	// woohoo, we loaded a file.
    	try {
    				 File file = chooser.getSelectedFile();
    				
    	 FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
           songs.clear();
           profanity.clear();
           profanity = (ArrayList)ois.readObject();
           songs = (ArrayList)ois.readObject();
           ois.close();
           ran = 1;
           modelT.fireTableStructureChanged();
           
           profTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
profTable.getColumnModel().getColumn(0).setPreferredWidth(120);
profTable.getColumnModel().getColumn(1).setPreferredWidth(120);
profTable.getColumnModel().getColumn(2).setPreferredWidth(70);

// 310 + (profanity.size - 1) * 70;
for ( int i = 0; i < profanity.size(); i++)
{
profTable.getColumnModel().getColumn(i+3).setPreferredWidth(70);
}

int leftover = 630 - 310;
// thats how much is left over after we do the first three columns

if ( (leftover + (profanity.size() * 70) ) < 630 )
{

int dif = (630 - leftover) - ((profanity.size() - 1) * 70);

profTable.getColumnModel().getColumn(2+profanity.size()).setPreferredWidth(dif+6);



}
           
           
             try {
 	URL url = this.getClass().getClassLoader().getResource("success.wav");
   
	 AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
         // Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.start();
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
           
           
           
           
           } catch ( Exception e )
           {
           }
    	
    			
    		}
    	
    	}
    	
    	
    	

   		 if ( runningProgram == 1)
   				{
   			
	JOptionPane.showMessageDialog(null, "The engine is already running. Please wait for the current action to be completed before requesting a load. Thank you.","Error", JOptionPane.ERROR_MESSAGE);
	return;
	
				}
	
   		
   		
   		} 
    
    
    
    
    
    
    
    if ( src == _save )
    {
    
    // first check to see if the program has processed anything...
    		if ( ran == 1 )
    		{
    				// we already ran
    		
    				JFileChooser FC=new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Explicitor Song Archive", "explic");
    		FC.setFileFilter(filter);
  		 
    				int result = FC.showSaveDialog(null);
    	
    				if ( result == JFileChooser.APPROVE_OPTION )
    				{
    				 File file = FC.getSelectedFile();
    				 try {
    				 // write to the file :D
    				 FileOutputStream s;
    				if ( FC.getFileFilter() == filter )
    				{
    				System.out.println(getExtension(file));
    				if ( getExtension(file).equals("explic") )
    				{
    					 s = new FileOutputStream(file);
    			
    				}
    				else
    				{
    				 s = new FileOutputStream(file.getCanonicalPath() + ".explic");
    				
    				}
    				    				}
    				else
    				{
    				 s = new FileOutputStream(file);
    				}
    				ObjectOutputStream str = new ObjectOutputStream(s);
    				str.writeObject(profanity);
    				str.writeObject(songs);
    				str.close();
    				
    				     try {
 	URL url = this.getClass().getClassLoader().getResource("success.wav");
   
	 AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
         // Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.start();
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
    				
    				}
    				catch (java.io.FileNotFoundException n)
    				{
    				// should we create the file..?
    				}
    				catch (java.io.IOException e )
    				{
    				// derp
    				}
    				
    				
    				}
    				
    
    
    		}
    		else
    		{
    		 JOptionPane.showMessageDialog(null, "The application has not yet generated anything to save.","Error", JOptionPane.ERROR_MESSAGE);	
    		}
    
    
    
    
    
    
    }
    
    
    if ( src == fatherTime)
    {
    // we've been reached by fathertime.. 
    
    deltaT++;
    
    
    if ( deltaT == 5)
    {
    		// if we've taken thirty seconds
    		if ( completedSongs != -1 )
    		{
    		
    		float average = (float)completedSongs / (float)deltaT;
    		
    		// let's extrapolate!
    		
    		int timeRemaining =  (int)((float)average *  (float)(totalCount - songs.size()));
    		// how many are remaining
    		
    		setGuessedTime(timeRemaining);
    		
    		
    		
    		
    		
    		}
    		else
    		{
    		
    		fatherTime.stop();
    		}
    		deltaT = 0;
    		completedSongs = 0;
    }
    

    
    }
 
   // if ( src == goButton )
    //{
     
  // }
   if (src == chooseIndex)
    {
    
    	    JFileChooser chooser = new JFileChooser();
    		FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "iTunes Music Library.xml", "xml");
    		chooser.setFileFilter(filter);
  		    int returnVal = chooser.showOpenDialog(this);
    		if(returnVal == JFileChooser.APPROVE_OPTION)
    			{
      				 System.out.println("You chose to open this file: " +
    		         chooser.getSelectedFile().getName());
     		         textField.setText(chooser.getSelectedFile().getPath());
            
    			} 
    			return;
    }
 	else if ( src == chooseProf ) 
 	  {
 	 JFileChooser choooser = new JFileChooser();
   FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Text Files", "txt", "text");
    choooser.setFileFilter(filter);
  int returnVal = choooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) 
    	{
      // System.out.println("You chose to open this file: " +
       //     choooser.getSelectedFile().getName());
            profField.setText(choooser.getSelectedFile().getPath());
 			
 			if ( this.profanity == null)
 			{
 			this.profanity = new ArrayList<String>();
 			}
 			this.profanity.clear();
 			try {
 			profanity.clear();
  // Open the file that is the first 
  // command line parameter
  FileInputStream fstream = new FileInputStream(choooser.getSelectedFile().getPath());
  // Get the object of DataInputStream
  DataInputStream in = new DataInputStream(fstream);
  BufferedReader br = new BufferedReader(new InputStreamReader(in));
  String strLine;
  //Read File Line By Line
  while ((strLine = br.readLine()) != null)   {
  // Print the content on the console
  profanity.add(strLine);
  }
  //Close the input stream
  in.close();
  }
  catch ( java.io.FileNotFoundException b)
  {
  System.out.println("better kill myself");
  }
  catch ( java.io.IOException b )
  {
  System.out.println("still think ending my life is a better option.");
  }
    		
 		}
 	return;
    }
    
  /*   if ( src == searchButton )
    {
    String text = searchField.getText();
       if (text.length() == 0) {
            sorter.setRowFilter(null);
         } else {
             try {
                 sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
             } catch (Exception pse) {
                 JOptionPane.showMessageDialog(null, "Bad regex pattern", 
                     "Bad regex pattern", JOptionPane.ERROR_MESSAGE);
             }
         }
    
    
    return;
    }
      */

     
  }


  public static void main(String[] args) {
    JFrame f = new EmptyFrame1();

 
f.setVisible(true);
  } //main
 

  
}	
	
	// <<---------------------->>
	class DownloadManager implements Runnable 
	{
	public String URL;
		public String song;
 	public String artist;
 	public int success;
 	public ArrayList<Integer> profanity;
	public int id;
	public SongData currentSong;
	public String lyrics;
		public EmptyFrame1 parent;
	
		int numberOfCursesForWord(String curse, String lyrics)
  {
  					lyrics = StringUtils.lowerCase(lyrics);
  					return StringUtils.countMatches(lyrics,curse);
 
//return countIndexOf(lyrics,curse);
  }
 
	public String download(URI address) 
	{
	URL url = null;
	try {
		url =  address.toURL();
		
		
URLConnection connection = url.openConnection(); 

BufferedReader rd;

InputStream stream = connection.getInputStream();

rd = new BufferedReader(new InputStreamReader(stream));

StringBuffer sb = new StringBuffer();
String line;
while ((line = rd.readLine()) != null){
    sb.append(line);
}
rd.close();

String resultedString = sb.toString();
return resultedString;
}
catch ( java.net.MalformedURLException e )
		{
		return "-";
		}
	catch( java.io.IOException d )
	{
	return "-";
	}	
}
	
	public int isValidURL(String f)
	{
		try 
		{
		URL a = new URL(f);
		
		}
		catch (MalformedURLException b)
		{
		return -1;
		}
		return 1;
	
	}
	public int countIndexOf(String text, String search) {
    int count = text.split(search).length - 1;
    return count;
}
	
public void run()
		{
		
		// song and artist NEED to be defined.
		// use 
		// System.out.println("running thread number " + id);
	
	//	JOptionPane.showMessageDialog(null, "Running..." + parent.parser.myTracks.size(),"Message", JOptionPane.ERROR_MESSAGE);

	
		
	for ( int i = 0; i < parent.parser.myTracks.size(); i++ )
	 {
	 song = parent.parser.myTracks.get(i).getName();
	 artist = parent.parser.myTracks.get(i).getArtist();
	 id = i;
	 profanity = new ArrayList<Integer>();
	 
 	try {	
 	//http://www.metrolyrics.com/search.php?category=ArtistTitle&search=call+me+maybe
	URI uri  = new URI("http","lyrics.wikia.com","/api.php","artist=" + artist + "&song=" + song + "&fmt=xml",null);	
  	  String page = download(uri);
  	  if ( page == "-" )
    	{
    	// download failed
    	this.success = -1;
    	parent.got(new SongData(this.song,this.artist,this.profanity,this.success,i));
    	continue;
    	
    	}
  	//  System.out.println(page);
        String notFound = "<lyrics>Not found</lyrics>";
        if (page.indexOf(notFound) == -1 )
        {
          // not found, 
      
          String m = "<url>";
        int index = page.indexOf(m);
        index += m.length();
        
        int index2 = page.indexOf("</url>");
        String temp;
	try {
	
        temp = page.substring(index,index2);
        }
        
        catch ( java.lang.StringIndexOutOfBoundsException b )
        {
        	this.success = -1;
        parent.got(new SongData(song,artist,profanity,success,i));
    		continue;
        }
        
        lyrics = temp;
        this.URL = temp;
        success = 1;
    page = null;
    	
    	// lets grab these lyrics, bitches
    	URL l = new URL(temp);
    	URI il = l.toURI();
    	String lyricsPage = download(il);
    	if ( lyricsPage == "-" )
    	{
    	// download failed
    	this.success = -1;
    	parent.got(new SongData(song,artist,profanity,success,i));
    	continue;
    	
    	}
    	//
    	
    	String marker = "height='17'/></a></div>";
    	// we need the second instance of this
    	
    	index = lyricsPage.indexOf(marker);
    	index += marker.length();
    	
    	//index2 = lyricsPage.indexOf(marker,index);
    	
    	
    	// index now holds the starting position of the HTML
    		
    	String endMarker = "<!--";
    	//printf("index: " + index + "; lPage = " + lyricsPage.indexOf(endMarker, index));
String HTML;
	try {
    	HTML = lyricsPage.substring(index,lyricsPage.indexOf(endMarker, index));
    	lyricsPage = null;
    	}
    	catch ( java.lang.StringIndexOutOfBoundsException b )
    	{
    	System.out.println("Invalid Lyrics: "+ this.song);
    	this.success = -1;
    	
    //	System.out.println("got1 called" + this.id);
    	parent.got(new SongData(song,artist,profanity,success,i));
    	continue;
    	}
    	
    	
    	// now we have the HTML string..
    	
    	
    	String lyrics_ = StringEscapeUtils.unescapeHtml4(HTML);
    	
    	this.lyrics = lyrics_;
    	
    	 	for ( int j = 0; j < parent.profanity.size(); j++)
	  		{
	  		
	  			int count =  numberOfCursesForWord(parent.profanity.get(j), this.lyrics);
  
	  			profanity.add(count);
	  	
	  	
	  		}
	
   // 	System.out.println("got2 called" + this.id);
	  parent.got(new SongData(this.song,this.artist,this.profanity,this.success,i,this.URL));
    		continue;
    	
    	
        
    
    
    
    
        }
       else if ( page.indexOf(notFound) > -1 )
        {
        // we have no lyrics!!
       //  System.out.println("(-1)");
            this.success = -1;
     SongData temp = new SongData(song,artist,profanity,success,i);
 //   	System.out.println("got3 called" + this.id);
      parent.got(temp);
    	  continue;
        
        }
      
        
  }

   	
  catch (java.net.URISyntaxException b)
	{
	this.success = -1;
	
    	System.out.println("got4 called" + this.id);
	parent.got(new SongData(this.song,this.artist,this.profanity,this.success,i));
    continue;
	//System.out.println("URI syntax exception thrown (0)");
	
	
	}
	catch ( java.net.MalformedURLException b )
	{
	
	this.success = -1;
	
    	System.out.println("got5 called" + this.id);
	parent.got(new SongData(this.song,this.artist,this.profanity,this.success,i));
    continue;
//	System.out.println("oh well. :(");
	}

		}
	
	
	
	}
	
	
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	class SAXParserExample extends DefaultHandler {

    public String LIBRARY_FILE_PATH = "/tmp/iTunes Music Library.xml"; //"C:\\iTunes Music Library.xml";

   public ArrayList<Song> myTracks;
//public DefaultListModel model;
    private String tempVal;

    //to maintain context
    private Song tempTrack;

    boolean foundTracks = false;

    private String previousTag;
    private String previousTagVal;

    public SAXParserExample() {
        myTracks = new ArrayList<Song>();
    }

    public void runExample() {
        parseDocument();
        printData();
        
    }

    public void parseDocument() {
        //get a factory
      //  model = new DefaultListModel();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse(LIBRARY_FILE_PATH, this);

        }catch(SAXException se) {
            se.printStackTrace();
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    public void printData(){

        System.out.println("No of Tracks '" + myTracks.size() + "'.");

        Iterator<Song> it = myTracks.iterator();

        while(it.hasNext()) {
            Song song = it.next();
            System.out.println(song.getAlbum() + " - " + song.getName());
        }
    }

    //Event Handlers
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";

        if (foundTracks) {
            if ("key".equals(previousTag) && "dict".equalsIgnoreCase(qName)) {
                //create a new instance of employee
                tempTrack = new Song();
               myTracks.add(tempTrack);
             //   model.add(tempTrack);
            }
        } else {
            if ("key".equals(previousTag) && "Tracks".equalsIgnoreCase(previousTagVal) && "dict".equalsIgnoreCase(qName)) {
                foundTracks = true; // We are now inside the Tracks dict.
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch,start,length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (foundTracks) {
            if (previousTagVal.equalsIgnoreCase("Name") && qName.equals("string"))
            {
                    tempTrack.setName(tempVal);
            }
            else if (previousTagVal.equalsIgnoreCase("Artist") && qName.equals("string"))
            {
                    tempTrack.setArtist(tempVal);
            }
            else if (previousTagVal.equalsIgnoreCase("Album") && qName.equals("string"))
            {
                    tempTrack.setAlbum(tempVal);
            }
            else if (previousTagVal.equalsIgnoreCase("Play Count") && qName.equals("integer"))
            {
                    Integer value = Integer.parseInt(tempVal);
                    tempTrack.setPlayCount(value.intValue());
            }

            // Mark when we come to the end of the "Tracks" dict.
            if ("key".equals(qName) && "Playlists".equalsIgnoreCase(tempVal)) {
                foundTracks = false;
            }
        }

        // Keep track of the previous tag so we can track the context when we're at the second tag in a key, value pair.
        previousTagVal = tempVal;
        previousTag = qName;
    }

    /**
     * A simple representation of a song in the iTunes library.
     */
    public class Song implements Comparable{

        private String name;
        private String artist;
        private String album;

        private int playCount;
		public int compareTo(Object o)
		{
		Song a = (Song)o;
		// compare to a;
		return a.getArtist().compareTo(getArtist());
		
		
		
		
		}
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artistName) {
            this.artist = artistName;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String albumName) {
            this.album = albumName;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }
    }

  //  public static void main(String[] args) {
    ///    SAXParserExample spe = new SAXParserExample();
       // spe.runExample();
   //  }

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	