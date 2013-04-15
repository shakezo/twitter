package main;

//import java.awt.Container;
import java.awt.Dimension;
//import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.io.File;
import java.io.UnsupportedEncodingException;
//import java.lang.Number;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shakezo_
 *
 */
public class Event implements ActionListener {

	private JPanel panel;
	private JLabel name,timing,lang,format;
	private JTextField textField_name,textField_timing;
	private JRadioButton radio1,radio2;
	private JButton startButton,stopButton;
	private ButtonGroup group;
	private JCheckBox ckbox_JSON,ckbox_csv;
	private boolean buttonOn;
	//private boolean check;
	private String  str;
	private Timer timer;
	private JFrame frame;

	//search query
	private String keyword;
	private int    stiming;
	private boolean json_slctd,csv_slctd;
	private String langJP;
	//private String[] strList=null;
   // private Pattern ptor =  Pattern.compile("\\s+OR\\s+");
   // private Pattern ptand = Pattern.compile("\\s+");
   // private long    latesttwid;
    private long since_id;
    private String filename;



	public Event(){
		this.buttonOn=false;
		//this.check=false;
		this.str="initstr";
		this.keyword=null; //検索キーワード
		this.stiming=0;
		this.since_id=0;  //取得したツイートの最新ID

	}

   public String getStr(){
	   return this.str;
   }

   public void setButtonon(){
	   this.buttonOn=true;
   }

	public boolean getButtonOn(){
		return this.buttonOn;
	}


	public void actionPerformed(ActionEvent e){
		//System.out.println("actioncom1="+timer.getActionCommand());
		String cmd = e.getActionCommand();
		if(cmd.equals("start")){
			System.out.println("ツイート取得開始");
			System.out.println("text=" +textField_name.getText());
			String keyword=textField_name.getText();
			this.str=keyword;
			if(keyword.equals("")){
				System.out.println("keyword is null");
				JOptionPane.showMessageDialog(this.frame, "検索キーワードを入力してください");
				return;
			}


			System.out.println("DEBUG01");
			try {
				this.timer = new Timer(3000 , this);
				this.timer.setInitialDelay(3000);
				this.timer.start();
				int n = Integer.parseInt(textField_timing.getText());
				this.stiming=n*1000;
				this.timer.setDelay(this.stiming);

				//timer.setActionCommand("timer");
			} catch (NumberFormatException ne) {
				JOptionPane.showMessageDialog(this.frame, "取得間隔に整数値を入力してください");
				return;
			}

			System.out.println("Timerモードに変更");



			this.timer.setActionCommand("timer");


			//検索クエリ作成
			//検索クエリの作成

			System.out.println("actioncom2="+timer.getActionCommand());
			System.out.println("timerDelay=" +timer.getDelay());

			//検索条件の確認
			char[] c={'\u3000'};
			String wspace=new String(c);
			keyword=keyword.replaceAll(wspace," ");

			String regex = "\\s+OR\\s+";
			String regex2 ="\\s+";
			String[] strList=null;
		    Pattern p = Pattern.compile(regex);
		    Matcher m = p.matcher(keyword);
		    Pattern p2 = Pattern.compile(regex2);

		    StringBuilder sb=new StringBuilder();
		    if(m.find()){
		    	strList=keyword.split("\\s+OR\\s+");
		    	for(int i=0;i<strList.length;i++){
		    		 Matcher m2 = p2.matcher(strList[i]);
		    		 if(m2.find()){
		    			 System.out.println("error");
		    			 JOptionPane.showMessageDialog(this.frame, "検索キーワードが不正です");
		    			 return;
		    		 }
		    		 try {
						strList[i]=URLEncoder.encode(strList[i] , "UTF-8");
						sb.append(strList[i]);
					} catch (UnsupportedEncodingException e2) {
						// TODO 自動生成された catch ブロック
						e2.printStackTrace();
					}
		    		 sb.append("+OR+");
		    	}
		    	sb.delete(sb.length() - 4, sb.length());
		    }else{
		    	try {
					sb.append(URLEncoder.encode(keyword , "UTF-8"));
				} catch (UnsupportedEncodingException e2) {
					// TODO 自動生成された catch ブロック
					e2.printStackTrace();
				}

		    }
		    //キーワードの更新
		    this.keyword=sb.toString();

		    //checkboxの確認
		    this.json_slctd=this.ckbox_JSON.isSelected();
		    this.csv_slctd=this.ckbox_csv.isSelected();
		    if(this.radio1.isSelected()){
		    	this.langJP="ja";
		    }else{
		    	this.langJP="all";
		    }


		    //filename
		    Date date1 = new Date();  //(1)Dateオブジェクトを生成
		    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddmmss");
		    String dirname="./data/json";
		    File newdir = new File(dirname);
		    newdir.mkdirs();
		    dirname="./data/csv";
		    newdir = new File(dirname);
		    newdir.mkdirs();

		    this.filename=keyword+"_"+sdf1.format(date1);

			stopButton.setEnabled(true);
		    startButton.setEnabled(false);



		    //timer.start();

		}


		if(cmd.equals("stop")){
		//if(timer.getActionCommand().equals("timer")){

			stopButton.setEnabled(false);
		    startButton.setEnabled(true);
		    this.since_id=0;
		    timer.stop();
			System.out.println("str="+this.str);
			System.out.println("ツイート取得終了");
		}
		if (cmd.equals("timer")){
		//if(timer.getActionCommand().equals("timer")){
			//tweetの取得開始
			System.out.println("TimerMode");
			TweetSearch ts =new TweetSearch(this.keyword,this.stiming,this.json_slctd,
					this.csv_slctd,this.langJP,this.since_id,this.filename);
			this.since_id=ts.doTweetSearch();

		}
		System.out.println("end of EL");
	}

	/**
	 * @return
	 * Parnelの詳細設定
	 */
	public JPanel createContentPane (){


		//Panelの作成
		this.panel =new JPanel();
		this.panel.setLayout(null);


		//Panelに表示する文字列を設定
		this.name = new JLabel("検索キーワード:");
		this.name.setPreferredSize(new Dimension(80,80));

		this.timing=new JLabel("検索間隔(sec):");
		this.timing.setPreferredSize(new Dimension(80,80));

		this.lang=new JLabel("検索対象");
		this.lang.setPreferredSize(new Dimension(80,80));

		this.format=new JLabel("保存形式");
		this.format.setPreferredSize(new Dimension(80,80));

		//表示文字列の位置を指定
		this.name.setLocation(10,20);
		this.name.setSize(110,30);

		this.timing.setLocation(10,60);
		this.timing.setSize(110,30);

		this.lang.setLocation(10,100);
		this.lang.setSize(110,30);

		this.format.setLocation(110,100);
		this.format.setSize(110,30);

		//各入力項目のテキストフィールドを設定
		this.textField_name = new JTextField(10);
		this.textField_name.setLocation(130,20);
		this.textField_name.setSize(200,30);


		this.textField_timing = new JTextField(10);
		this.textField_timing.setLocation(130,60);
		this.textField_timing.setSize(50,30);

		//Panelに項目を追加
		this.panel.add(name);
		this.panel.add(timing);
		this.panel.add(lang);
		this.panel.add(format);
		this.panel.add(textField_name);
		this.panel.add(textField_timing);

		//Container contentPane =getContentPane();
		//contentPane.add(panel);
		this.radio1 = new JRadioButton("日本語",true);
	    this.radio2 = new JRadioButton("指定しない");


	    //panel.add(radio1)
		this.group = new ButtonGroup();
		this.group.add(radio1);
		this.group.add(radio2);

		this.radio1.setLocation(10,120);
		this.radio1.setSize(100,30);

		this.radio2.setLocation(10,150);
		this.radio2.setSize(100,30);

		//LineBorder border = new LineBorder(Color.PINK, 2, true);
		//radio1.setBorder(border);
		//radio1.setBorderPainted(true);
		this.panel.add(radio1);
		this.panel.add(radio2);


		//JSON出力用チェックボックスの作成
		this.ckbox_JSON = new JCheckBox("JSON",true);
		this.ckbox_JSON.setText("JSON");
		this.ckbox_JSON.setLocation(110,120);
		this.ckbox_JSON.setSize(100,30);
		this.panel.add(ckbox_JSON);


		//CSV出力用チェックボックスの作成
		this.ckbox_csv = new JCheckBox("CSV",true);
		this.ckbox_csv.setText("CSV");
		this.ckbox_csv.setLocation(110,150);
		this.ckbox_csv.setSize(140,30);
		this.panel.add(ckbox_csv);

		//集取開始ボタンと終了ボタンの作成
		this.startButton = new JButton("収集開始");
		this.stopButton = new JButton("終了");

		//収集開始ボタンと終了ボタンの配置
		this.startButton.setLocation(10,210);
		this.startButton.setSize(100,30);
		//button_start.addActionListener(new myListener1());
		this.startButton.addActionListener(this);
		this.startButton.setActionCommand("start");

		this.stopButton.setLocation(220,210);
		this.stopButton.setSize(60,30);
		this.stopButton.addActionListener(this);
		this.stopButton.setActionCommand("stop");
		//収集開始ボタンと終了ボタンをPanelに配置
		this.panel.add(startButton);
		this.panel.add(stopButton);

		this.stopButton.setEnabled(false);

		//Timerの設定
		//System.out.println("タイマー設定");
		//this.timer = new Timer(300 , this);
		//this.timer.setInitialDelay(300);
		//this.timer.start();
		//timer.setActionCommand("timer");

		return panel;
	}


	/**
	 * 設定パネルの設定
	 */
	public void createAndShowGUI() {
	    frame = new JFrame("あつめるったー ver0.90");
		//Event demo = new Event();
	    //Event demo=this;
		frame.setContentPane(this.createContentPane());
		frame.setBounds(100,100,350,300);//Frameのサイズ設定
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); //JFrame可視化
		System.out.println("setting completed...");
	}
}
