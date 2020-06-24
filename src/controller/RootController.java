package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Student;

public class RootController implements Initializable {
	@FXML
	private TableView tableView;
	@FXML
	private TextField txtName;
	@FXML
	private ComboBox cmbLevel;
	@FXML
	private TextField txtBan;
	@FXML
	private Button btnTotal;
	@FXML
	private Button btnAvg;
	@FXML
	private Button btnInit;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private TextField txtKo;
	@FXML
	private TextField txtEng;
	@FXML
	private TextField txtMath;
	@FXML
	private TextField txtSic;
	@FXML
	private TextField txtSoc;
	@FXML
	private TextField txtMusic;
	@FXML
	private TextField txtTotal;
	@FXML
	private TextField txtAvg;
	@FXML
	private RadioButton rdoMale;
	@FXML
	private RadioButton rdoFemale;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnChart;
	@FXML
	private Button btnList;
	@FXML
	private DatePicker dpkDate;
	@FXML
	private ImageView imgView;
	@FXML
	private Button btnImageFile;

	public Stage stage;
	private ObservableList<Student> obsList;
	private ToggleGroup group;
	private int tableViewSelectedIndex;
	private File selectFile;
	private File directorySave;

	public RootController() {
		this.obsList = FXCollections.observableArrayList();
		this.stage = null;
	}

	// 이벤트 등록 및 처리, UI객체 초기화 세팅
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		insertBasicData(); // 기본 입력 데이터 처리함수
		tableViewColumnInitialize(); // 테이블뷰 UI객체 컬럼 초기화 셋팅(컬럼 14개 만들고 Student 필드와 연결)
		comboBoxLevelInitialize(); // 학년을 입력하는 초기화 처리
		radioButtonGenderInitialze(); // 성별 라디오 버튼 그룹 초기화 처리
		textFieldNumberFormat(); // 점수 입력창에 3자리 수까지 입력 0~100까지의 정수만 입력 할 수 있도록 세팅
		totalLoadList(); // studentDB 데이터 베이스에서 gradeTBL에서 전체를 가져오는 이벤트
		setDirectorySaveImage();// 사진 저장할 수 있는 폴더 만들기

		setDefaultImageView(); // 이미지 사진 등록
		btnTotal.setOnAction(event -> handleBtnTotalAction(event)); // 총점 이벤트 등록 핸들러함수처리
		btnAvg.setOnAction(event -> handleBtnAvgAction(event)); // 평균 이벤트 등록 핸들러함수 처리
		btnInit.setOnAction(event -> handleBtnInitAction(event)); // 초기화버튼 이벤트 등록 핸들러 함수
		btnOk.setOnAction(event -> handleBtnOkAction(event)); // 등록버튼 이벤트 등록 핸들러 함수
		btnSearch.setOnAction(event -> handleBtnSearchAction(event)); // 찾기 버튼을 누르면 검색되는 이벤트
		btnDelete.setOnAction(event -> handleBtnDeleteAction(event)); // 테이블 삭제 버튼
		tableView.setOnMousePressed(event -> handleTableViewPressedAction(event)); // tableView 선택했을 때 이벤트 등록
		btnEdit.setOnAction(event -> handleBtnEditAction(event));// 수정 버튼 이벤트 핸들러 등록
		btnList.setOnAction(event -> handleBtnListAction(event));// 리스트 버튼 이벤트 핸들러 등록
		btnChart.setOnAction(event -> handleBtnChartAction(event));// 차트 버튼 이벤트 핸들러 등록
		btnImageFile.setOnAction(event -> handleBtnImageFileAction(event));// 이미지 버튼 이벤트 핸들러 등록
		tableView.setOnMouseClicked(event -> handlePieChartAction(event));// 파이차트 이벤트 등록 핸들러 함수 처리(더블클릭하면 발생)
		btnExit.setOnAction(event -> stage.close()); // 등록버튼 창닫기 핸들러 함수
	}

	// ****************************************************************핸들러함수**************************************************************
	// 사진 저장할 수 있는 폴더 만들기 C드라이브에 images라는 폴더 생성 후 여기서 관리
	private void setDirectorySaveImage() {
		directorySave = new File("C:/images");
		if (!directorySave.exists()) { // 있냐?를 묻는데 낫을 줘서 없으면 만듬
			directorySave.mkdir();
			System.out.println("C:/images 디렉토리 이미지 생성 성공");
		}
	}

	// 이미지 버튼 이벤트 핸들러 등록
	private void handleBtnImageFileAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		selectFile = fileChooser.showOpenDialog(stage);// 선택된 파일을 돌려줌.

		try {
			if (selectFile != null) {
				String localURL = selectFile.toURI().toURL().toString();// 사진의 실제 경로가 문자열로 전환된다.
				Image image = new Image(localURL, false);
				imgView.setImage(image);
			}
		} catch (MalformedURLException e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("이미지 로드 오류");
			alert.setHeaderText("확인 바랍니다.");
			alert.setContentText("주의!");
			alert.showAndWait();
		}
	}

	// 이미지 사진 등록
	private void setDefaultImageView() {
		Image image = new Image("/images/default.jpg", false);
		imgView.setImage(image);
	}

	// 리스트 버튼 이벤트 핸들러 등록
	private void handleBtnListAction(ActionEvent event) {
		obsList.clear(); // 매개변수로 자신을 주면 자기가 없어짐
		totalLoadList();
	}

	// mysql 드라이버 로드 : studentDB 데이터 베이스에서 gradeTBL에서 전체를 가져오는 이벤트
	private void totalLoadList() {
		StudentDAO studentDAO = new StudentDAO();
		ArrayList<Student> arrayList = studentDAO.getTotalLoadList();

		if (arrayList == null)
			return;

		// arrayList에 있는 값을 obsList에 입력한다.
		for (int i = 0; i < arrayList.size(); i++) {
			Student s = arrayList.get(i);
			obsList.add(s);
		}
		tableView.setItems(obsList);
	}

	// 파이차트 이벤트 등록 핸들러 함수 처리(더블클릭하면 발생)
	private void handlePieChartAction(MouseEvent event) {// event에 이벤트가 발생한 모든 정보가 넘어옴
		//
		if (event.getClickCount() != 2)
			return;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/pichart.fxml"));
			Scene scene = new Scene(root);
			Stage pieChartStage = new Stage(StageStyle.UTILITY);

			// 이벤트 등록 및 처리
			PieChart pieChart = (PieChart) scene.lookup("#pieChart");
			Button btnClose = (Button) scene.lookup("#btnClose");

			// 2번 클릭된 학생 객체를 가져옴
			Student student = obsList.get(tableViewSelectedIndex);

			// 차트에 입력할 내용을 observableList에 입력
			ObservableList pieChartObsList = FXCollections.observableArrayList();

			//
			pieChartObsList.add(new PieChart.Data("국어", Integer.parseInt(student.getKorean())));
			pieChartObsList.add(new PieChart.Data("영어", Integer.parseInt(student.getEnglish())));
			pieChartObsList.add(new PieChart.Data("수학", Integer.parseInt(student.getMath())));
			pieChartObsList.add(new PieChart.Data("사회", Integer.parseInt(student.getSoc())));
			pieChartObsList.add(new PieChart.Data("과학", Integer.parseInt(student.getSic())));
			pieChartObsList.add(new PieChart.Data("음악", Integer.parseInt(student.getMusic())));
			pieChart.setData(pieChartObsList);

			pieChartStage.initModality(Modality.WINDOW_MODAL);
			pieChartStage.initOwner(stage);
			pieChartStage.setScene(scene);
			pieChartStage.setTitle("개인 성적 차트 보고서 : 파이 차트");
			pieChartStage.show();

			btnClose.setOnAction(event1 -> pieChartStage.close());
		} catch (IOException e) {
		}
	}

	// 초기값 설정 함수
	private void insertBasicData() {
		txtName.setText("aaa");
		cmbLevel.getSelectionModel().select(0);
		txtBan.setText("2");
		txtKo.setText("90");
		txtMath.setText("90");
		txtEng.setText("90");
		txtSic.setText("90");
		txtSoc.setText("90");
		txtMusic.setText("90");
	}

	// 차트 버튼 이벤트 핸들러 등록
	private void handleBtnChartAction(ActionEvent event) {
		// 내용 가져와서, stage scene 생성
		try {
			if (obsList.size() == 0)
				throw new Exception();

			Parent root = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Scene scnen = new Scene(root);
			Stage barStage = new Stage(StageStyle.UTILITY);

			// 이벤트 핸들러 등록
			BarChart barChart = (BarChart) scnen.lookup("#barChart");
			Button btnClose = (Button) scnen.lookup("#btnClose");

			// 먼저 차트는 XYchart 시리즈를 만든다. (국어)
			XYChart.Series seriesKorean = new XYChart.Series();
			seriesKorean.setName("국어");
			ObservableList koreanList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				koreanList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesKorean.setData(koreanList);
			barChart.getData().add(seriesKorean);

			// 먼저 차트는 XYchart 시리즈를 만든다. (영어)
			XYChart.Series seriesEnglish = new XYChart.Series();
			seriesEnglish.setName("영어");
			ObservableList englishList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				englishList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesEnglish.setData(englishList);
			barChart.getData().add(seriesEnglish);

			// 먼저 차트는 XYchart 시리즈를 만든다. (수학)
			XYChart.Series seriesMath = new XYChart.Series();
			seriesMath.setName("수학");
			ObservableList mathList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				mathList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesMath.setData(mathList);
			barChart.getData().add(seriesMath);

			// 먼저 차트는 XYchart 시리즈를 만든다. (과회)
			XYChart.Series seriesScience = new XYChart.Series();
			seriesScience.setName("과학");
			ObservableList scienceList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				scienceList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesScience.setData(scienceList);
			barChart.getData().add(seriesScience);

			// 먼저 차트는 XYchart 시리즈를 만든다. (사회)
			XYChart.Series seriesSociety = new XYChart.Series();
			seriesSociety.setName("사학");
			ObservableList societyList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				societyList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesSociety.setData(societyList);
			barChart.getData().add(seriesSociety);

			// 먼저 차트는 XYchart 시리즈를 만든다. (음악)
			XYChart.Series seriesMusic = new XYChart.Series();
			seriesMusic.setName("음악");
			ObservableList musicList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				musicList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesMusic.setData(musicList);
			barChart.getData().add(seriesMusic);

			//
			btnClose.setOnAction(event1 -> barStage.close());

			barStage.initModality(Modality.WINDOW_MODAL);
			barStage.initOwner(this.stage);
			barStage.setResizable(false);
			barStage.setScene(scnen);
			barStage.setTitle("성적 막대 그래프");
			barStage.show();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("데이터 리스트가 없습니다.");
			alert.setHeaderText("tableView 리스트 입력요망");
			alert.setContentText("주의!");
			alert.showAndWait();
		}

	}

	// 점수 입력창에 3자리 수까지 입력 0~100까지의 정수만 입력 할 수 있도록 세팅
	private void textFieldNumberFormat() {
		DecimalFormat decimalFormat = new DecimalFormat("###"); // 십진수 3자리까지만 받겠다!. decimal이 십진수 라는 뜻, ### 3개 3자리를 의미
		txtKo.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0주면 if문에서 숫자다 100보다 커야 하는데 안크기 때문에 방어 안됨
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("점수입력 요망");
				alert.setHeaderText("점수(0~100)를 입력하십시오");
				alert.setContentText("숫자 외 입력 불가!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));
		txtEng.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0주면 if문에서 숫자다 100보다 커야 하는데 안크기 때문에 방어 안됨
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("점수입력 요망");
				alert.setHeaderText("점수(0~100)를 입력하십시오");
				alert.setContentText("숫자 외 입력 불가!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));
		txtMath.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0주면 if문에서 숫자다 100보다 커야 하는데 안크기 때문에 방어 안됨
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("점수입력 요망");
				alert.setHeaderText("점수(0~100)를 입력하십시오");
				alert.setContentText("숫자 외 입력 불가!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));
		txtSic.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0주면 if문에서 숫자다 100보다 커야 하는데 안크기 때문에 방어 안됨
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("점수입력 요망");
				alert.setHeaderText("점수(0~100)를 입력하십시오");
				alert.setContentText("숫자 외 입력 불가!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));
		txtSoc.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0주면 if문에서 숫자다 100보다 커야 하는데 안크기 때문에 방어 안됨
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("점수입력 요망");
				alert.setHeaderText("점수(0~100)를 입력하십시오");
				alert.setContentText("숫자 외 입력 불가!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));
		txtMusic.setTextFormatter(new TextFormatter<>(e -> {
			// 글자를 입력해서 빈 '공백'이면 다시 이벤트를 돌려줌
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// 위치조사( 키보드 치는 위치를 추적한다.)
			ParsePosition parsePosition = new ParsePosition(0);
			// 숫자만 받겠다.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0주면 if문에서 숫자다 100보다 커야 하는데 안크기 때문에 방어 안됨
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
				alert.setTitle("점수입력 요망");
				alert.setHeaderText("점수(0~100)를 입력하십시오");
				alert.setContentText("숫자 외 입력 불가!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // 입력한 값을 안받겠다. 숫자가 아니면 안받겠다. 3자리 숫자만 받겠다.
			} else {
				return e;
			}
		}));

	}

	// 수정 버튼 이벤트 핸들러 등록
	private void handleBtnEditAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/formedit.fxml"));

			TextField txtNo = (TextField) root.lookup("#txtNo");
			TextField txtName = (TextField) root.lookup("#txtName");
			TextField txtYear = (TextField) root.lookup("#txtYear");
			TextField txtBan = (TextField) root.lookup("#txtBan");
			TextField txtGender = (TextField) root.lookup("#txtGender");
			TextField txtKorean = (TextField) root.lookup("#txtKorean");
			TextField txtEnglish = (TextField) root.lookup("#txtEnglish");
			TextField txtMath = (TextField) root.lookup("#txtMath");
			TextField txtSic = (TextField) root.lookup("#txtSic");
			TextField txtSoc = (TextField) root.lookup("#txtSoc");
			TextField txtMusic = (TextField) root.lookup("#txtMusic");
			TextField txtTotal = (TextField) root.lookup("#txtTotal");
			TextField txtAvg = (TextField) root.lookup("#txtAvg");
			Button btnCal = (Button) root.lookup("#btnCal");
			Button btnFormAdd = (Button) root.lookup("#btnFormAdd");
			Button btnFormCancel = (Button) root.lookup("#btnFormCancel");

			// tebleView 에서 선택된 위치값을 가지고 observableList에서 그 위치를 찾아서 해당 Student 객체를 가져오면 됨.
			Student student = obsList.get(tableViewSelectedIndex);
			txtNo.setText(String.valueOf(student.getNo()));
			txtName.setText(student.getName());
			txtYear.setText(student.getLevel());
			txtBan.setText(student.getBan());
			txtGender.setText(student.getGender());
			txtKorean.setText(student.getKorean());
			txtEnglish.setText(student.getEnglish());
			txtMath.setText(student.getMath());
			txtSic.setText(student.getSic());
			txtSoc.setText(student.getSoc());
			txtMusic.setText(student.getMusic());
			txtTotal.setText(student.getTotal());
			txtAvg.setText(student.getAvg());

			// txtNo 텍스트 필드를 readOnly 로 만든다. (읽기만 가능한 상태)(번호,이름,학년, 반)
			txtNo.setDisable(true);
			txtName.setDisable(true);
			txtYear.setDisable(true);
			txtBan.setDisable(true);
			txtGender.setDisable(true);

			Scene scene = new Scene(root);
			Stage editStage = new javafx.stage.Stage(StageStyle.UTILITY); // 임시객체에서 사용할 때는 fianl이 됨.

			// 수정창에 해당된 이벤트 등록 및 핸들러 처리 기능
			btnCal.setOnAction((ActionEvent event1) -> {
				try {
					int korean = Integer.parseInt(txtKorean.getText());
					int English = Integer.parseInt(txtEnglish.getText());
					int Math = Integer.parseInt(txtMath.getText());
					int Sic = Integer.parseInt(txtSic.getText());
					int Soc = Integer.parseInt(txtSoc.getText());
					int Music = Integer.parseInt(txtMusic.getText());
					int total = korean + English + Math + Sic + Soc + Music;
					double avg = total / 6.0;
					txtTotal.setText(String.valueOf(total));
					txtAvg.setText(String.format("%.1f", avg));
				} catch (NumberFormatException e) {
					Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
					alert.setTitle("값 입력 요망");
					alert.setHeaderText("입력 되지 않은 값이 있습니다.");
					alert.setContentText("주의하세요!");
					alert.showAndWait();
				}
			});

			// 저장창에 해당된 이벤트 등록 및 핸들러 처리 기능
			btnFormAdd.setOnAction((ActionEvent event1) -> {
				Student student1 = obsList.get(tableViewSelectedIndex);
//				student1.setName(txtName.getText());
//				student1.setLevel(txtYear.getText());
//				student1.setBan(txtBan.getText());
//				student1.setGender(txtGender.getText());
				student1.setKorean(txtKorean.getText());
				student1.setEnglish(txtEnglish.getText());
				student1.setMath(txtMath.getText());
				student1.setSic(txtSic.getText());
				student1.setSoc(txtSoc.getText());
				student1.setMusic(txtMusic.getText());
				student1.setTotal(txtTotal.getText());
				student1.setAvg(txtAvg.getText());

				// mysql 드라이버 로드
				StudentDAO studentDAO = new StudentDAO();
				int returnValue = studentDAO.getStudentUpdate(student1);

				if (returnValue != 0)
					obsList.set(tableViewSelectedIndex, student1); // tableView obsList에 해당된 위치에 수정된 객체를 저장
				// observableList에 선택된 위치를 수정된 객체Student 집어 넣었다.
				editStage.close();
			});

			// stage 생성 및 설정
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(stage);
			editStage.setTitle("수정");
			editStage.setScene(scene);
			editStage.setResizable(false);
			editStage.show();
			// 취소 버튼 누루면 창 닫기
			btnFormCancel.setOnAction((ActionEvent event1) -> editStage.close());

		} catch (IndexOutOfBoundsException | IOException e) { // 한번에 예외 2개 주기
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("수정 에러");
			alert.setHeaderText("수정할 내용이 없습니다.");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}
	}

	// tableView 선택했을 때 이벤트 등록
	private void handleTableViewPressedAction(MouseEvent event) {
		tableViewSelectedIndex = tableView.getSelectionModel().getSelectedIndex();
	}

	// 테이블 삭제 버튼
	private void handleBtnDeleteAction(ActionEvent event) {
		StudentDAO studentDAO = new StudentDAO();
		// ?에 무엇을 지워야할지 해당된 쿼리문에 no 번호를 연결.
		Student student = obsList.get(tableViewSelectedIndex);
		int no = student.getNo();
		int returnValue = studentDAO.getStudentDelete(no);

		if (returnValue != 0) {
			// 이미지 폴더에서 삭제대상이 된 사진을 삭제해야된다.
			// 1. 삭제할 이미지 파일명을 찾는다.
			String fileName = student.getFileName();// 얘는 이미지 명 만 있음, 그래서 경로를 찾아야함, directorySave에 경로가 있음
			File fileDelete = new File(directorySave.getAbsolutePath() + "\\" + fileName);

			// 경로를 잘 가져왔는지 확인, 정말 그게 이미지 파일인지 확인
			if (fileDelete.exists() && fileDelete.isFile()) {
				fileDelete.delete();
			}
			obsList.remove(tableViewSelectedIndex);
		}
	}

	// 찾기 버튼을 누르면 검색되는 이벤트
	private void handleBtnSearchAction(ActionEvent event) {

		// 테이블뷰에 들어있는 데이터 제공 obsList
		try {
			StudentDAO studentDAO = new StudentDAO();

			if (txtSearch.getText().trim().equals(""))
				throw new Exception();
			ArrayList<Student> arrayList = studentDAO.getStudentFind(txtSearch.getText().trim());

			// arrayList에 있는 값을 obsList에 입력한다.
			if (arrayList.size() != 0) {
				obsList.clear();
				for (int i = 0; i < arrayList.size(); i++) {
					Student s = arrayList.get(i);
					obsList.add(s);
				}
			}
			tableView.setItems(obsList);

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("이름 입력 요망");
			alert.setHeaderText("이름을 입력하십시오");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}
	}

	// 성별 라디오 버튼 그룹 초기화 처리
	private void radioButtonGenderInitialze() {
		group = new ToggleGroup();
		rdoMale.setToggleGroup(group);
		rdoFemale.setToggleGroup(group);
//		rdoMale.setSelected(true);

	}

	// 학년을 입력하는 초기화 처리
	private void comboBoxLevelInitialize() {
		ObservableList<String> obsList = FXCollections.observableArrayList();
		obsList.addAll("1", "2", "3", "4", "5", "6");
		cmbLevel.setItems(obsList);
	}

	// 등록버튼 이벤트 등록 핸들러 함수

	private void handleBtnOkAction(ActionEvent event) throws NullPointerException {
		StudentDAO studentDAO = new StudentDAO();

		// 이미지 저장 처리 순서 1. 이미지 파일명을 생성해서 복사, 해당 디렉토리에 저장
		// 1. 이미지 파일명 생성

		if (selectFile == null) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("사진 선택 요망");
			alert.setHeaderText("사진(이미지 파일)을 선택하십시오");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
			return;
		}
		// 2. 실제 파일을 가져와서 새로만든 이미지 파일명에 저장.
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String fileName = null;
		try {
			fileName = "stu" + System.currentTimeMillis() + selectFile.getName();

			// 이미지 파일은 바이트 스트림릉 바꾸어서 버퍼를 사용
			// c드라이브에, images/stu1231231홀길동.jpg로 저장 하려함
			bis = new BufferedInputStream(new FileInputStream(selectFile));
			bos = new BufferedOutputStream(new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));

			// 이미지 파일을 읽어서 저장위치에 있는 파일에다가 쓴다.
			// -1은 더이상 읽을게 없다는 뜻
			int data = -1; // 파일을 읽어서 잘목 읽으면 -1을 돌려줌
			while ((data = bis.read()) != -1) { // 한번 복사하면 10000번이 돌아감, 한바이트씩 복사를 함.
				bos.write(data);
				bos.flush(); // 버퍼에 있는 값을 다 저장하기 위해 보내라.
			}
		} catch (Exception e) {
			System.out.println("파일 복사 에러" + e.getMessage());
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				System.out.println("bis.close(), bos.close() error " + e.getMessage());
			}
		}
		try {
			if (dpkDate.getValue().toString().trim().equals("")) {
				System.out.println("날짜를 입력해주세요.");
				return;
			}
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("모두 입력 요망");
			alert.setHeaderText("날짜를 선택하세요");
			alert.setContentText("날짜가 선택되지 않았습니다.");
			alert.showAndWait();
		}
		Student s = null;
		// ?에 무엇을 지워야할지 해당된 쿼리문에 no 번호를 연결.
		try {
			s = new Student(txtName.getText(), cmbLevel.getSelectionModel().getSelectedItem().toString(),
					txtBan.getText(), ((RadioButton) group.getSelectedToggle()).getText(), txtKo.getText(),
					txtEng.getText(), txtMath.getText(), txtSic.getText(), txtSoc.getText(), txtMusic.getText(),
					txtTotal.getText(), txtAvg.getText(), dpkDate.getValue().toString(), fileName);
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("모두 입력 요망");
			alert.setHeaderText("모두 입력 하십시오");
			alert.setContentText("입력되지 않은 값이 있습니다. \n 확인 바랍니다.");
			alert.showAndWait();
		}
		int returnValue = studentDAO.getStudentRegistry(s);
		if (returnValue != 0) {
			obsList.clear();
			totalLoadList();
			setDefaultImageView();
		}
	}

	// 초기화버튼 이벤트 등록 핸들러 함수
	private void handleBtnInitAction(ActionEvent event) {
		txtName.clear();
		cmbLevel.getSelectionModel().clearSelection();
		txtBan.clear();
		txtKo.clear();
		txtEng.clear();
		txtMath.clear();
		txtSic.clear();
		txtSoc.clear();
		txtMusic.clear();
		txtTotal.clear();
		txtAvg.clear();
		rdoMale.setSelected(false);
		rdoFemale.setSelected(false);
	}

	// 평균 이벤트 등록 핸들러함수 처리
	private void handleBtnAvgAction(ActionEvent event) {
		try {
			double avg = Integer.parseInt(txtTotal.getText()) / 6.0;
			txtAvg.setText(String.format("%.1f", avg));
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("총점 입력 요망");
			alert.setHeaderText("총점을 입력하십시오");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}
	}

	// 총점 이벤트 등록 핸들러함수처리
	private void handleBtnTotalAction(ActionEvent event) {
		try {
			int kor = Integer.parseInt(txtKo.getText());
			int eng = Integer.parseInt(txtEng.getText());
			int math = Integer.parseInt(txtMath.getText());
			int sic = Integer.parseInt(txtSic.getText());
			int soc = Integer.parseInt(txtSoc.getText());
			int music = Integer.parseInt(txtMusic.getText());
			int total = kor + eng + math + music + sic + soc;
			txtTotal.setText(String.valueOf(total));
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, 화면내용이 alert안에 다 만들어짐()고정화 되어있음
			alert.setTitle("점수입력 요망");
			alert.setHeaderText("점수를 입력하십시오");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}
	}

	// 테이블뷰 UI객체 컬럼 초기화 셋팅(컬럼 11개 만들고 Student 필드와 연결) - 모듈화한것
	private void tableViewColumnInitialize() {
		TableColumn colNo = new TableColumn("No");
		colNo.setMaxWidth(30);
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colName = new TableColumn("성명");
		colName.setMaxWidth(70);
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colLevel = new TableColumn("학년");
		colLevel.setMaxWidth(40);
		colLevel.setCellValueFactory(new PropertyValueFactory("level"));

		TableColumn colBan = new TableColumn("반");
		colBan.setMaxWidth(30);
		colBan.setCellValueFactory(new PropertyValueFactory<>("ban"));

		TableColumn colGender = new TableColumn("성별");
		colGender.setMaxWidth(40);
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

		TableColumn colKorean = new TableColumn("국어");
		colKorean.setMaxWidth(39);
		colKorean.setCellValueFactory(new PropertyValueFactory<>("korean"));

		TableColumn colEnglish = new TableColumn("영어");
		colEnglish.setMaxWidth(39);
		colEnglish.setCellValueFactory(new PropertyValueFactory<>("english"));

		TableColumn colMath = new TableColumn("수학");
		colMath.setMaxWidth(39);
		colMath.setCellValueFactory(new PropertyValueFactory<>("math"));

		TableColumn colSic = new TableColumn("과학");
		colSic.setMaxWidth(39);
		colSic.setCellValueFactory(new PropertyValueFactory<>("sic"));

		TableColumn colSoc = new TableColumn("사회");
		colSoc.setMaxWidth(39);
		colSoc.setCellValueFactory(new PropertyValueFactory<>("soc"));

		TableColumn colMusic = new TableColumn("음악");
		colMusic.setMaxWidth(39);
		colMusic.setCellValueFactory(new PropertyValueFactory<>("music"));

		TableColumn colTotal = new TableColumn("총점");
		colTotal.setMaxWidth(50);
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

		TableColumn colAvg = new TableColumn("평균");
		colAvg.setMaxWidth(70);
		colAvg.setCellValueFactory(new PropertyValueFactory<>("avg"));

		TableColumn colRegister = new TableColumn("등록일");
		colRegister.setMaxWidth(90);
		colRegister.setCellValueFactory(new PropertyValueFactory<>("register"));

		TableColumn colFileName = new TableColumn("이미지 경로");
		colFileName.setMaxWidth(260);
		colFileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		
		tableView.getColumns().addAll(colNo, colName, colLevel, colBan, colGender, colKorean, colEnglish, colMath,
				colSic, colSoc, colMusic, colTotal, colAvg, colRegister, colFileName);
	}
}
