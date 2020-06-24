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

	// �̺�Ʈ ��� �� ó��, UI��ü �ʱ�ȭ ����
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		insertBasicData(); // �⺻ �Է� ������ ó���Լ�
		tableViewColumnInitialize(); // ���̺�� UI��ü �÷� �ʱ�ȭ ����(�÷� 14�� ����� Student �ʵ�� ����)
		comboBoxLevelInitialize(); // �г��� �Է��ϴ� �ʱ�ȭ ó��
		radioButtonGenderInitialze(); // ���� ���� ��ư �׷� �ʱ�ȭ ó��
		textFieldNumberFormat(); // ���� �Է�â�� 3�ڸ� ������ �Է� 0~100������ ������ �Է� �� �� �ֵ��� ����
		totalLoadList(); // studentDB ������ ���̽����� gradeTBL���� ��ü�� �������� �̺�Ʈ
		setDirectorySaveImage();// ���� ������ �� �ִ� ���� �����

		setDefaultImageView(); // �̹��� ���� ���
		btnTotal.setOnAction(event -> handleBtnTotalAction(event)); // ���� �̺�Ʈ ��� �ڵ鷯�Լ�ó��
		btnAvg.setOnAction(event -> handleBtnAvgAction(event)); // ��� �̺�Ʈ ��� �ڵ鷯�Լ� ó��
		btnInit.setOnAction(event -> handleBtnInitAction(event)); // �ʱ�ȭ��ư �̺�Ʈ ��� �ڵ鷯 �Լ�
		btnOk.setOnAction(event -> handleBtnOkAction(event)); // ��Ϲ�ư �̺�Ʈ ��� �ڵ鷯 �Լ�
		btnSearch.setOnAction(event -> handleBtnSearchAction(event)); // ã�� ��ư�� ������ �˻��Ǵ� �̺�Ʈ
		btnDelete.setOnAction(event -> handleBtnDeleteAction(event)); // ���̺� ���� ��ư
		tableView.setOnMousePressed(event -> handleTableViewPressedAction(event)); // tableView �������� �� �̺�Ʈ ���
		btnEdit.setOnAction(event -> handleBtnEditAction(event));// ���� ��ư �̺�Ʈ �ڵ鷯 ���
		btnList.setOnAction(event -> handleBtnListAction(event));// ����Ʈ ��ư �̺�Ʈ �ڵ鷯 ���
		btnChart.setOnAction(event -> handleBtnChartAction(event));// ��Ʈ ��ư �̺�Ʈ �ڵ鷯 ���
		btnImageFile.setOnAction(event -> handleBtnImageFileAction(event));// �̹��� ��ư �̺�Ʈ �ڵ鷯 ���
		tableView.setOnMouseClicked(event -> handlePieChartAction(event));// ������Ʈ �̺�Ʈ ��� �ڵ鷯 �Լ� ó��(����Ŭ���ϸ� �߻�)
		btnExit.setOnAction(event -> stage.close()); // ��Ϲ�ư â�ݱ� �ڵ鷯 �Լ�
	}

	// ****************************************************************�ڵ鷯�Լ�**************************************************************
	// ���� ������ �� �ִ� ���� ����� C����̺꿡 images��� ���� ���� �� ���⼭ ����
	private void setDirectorySaveImage() {
		directorySave = new File("C:/images");
		if (!directorySave.exists()) { // �ֳ�?�� ���µ� ���� �༭ ������ ����
			directorySave.mkdir();
			System.out.println("C:/images ���丮 �̹��� ���� ����");
		}
	}

	// �̹��� ��ư �̺�Ʈ �ڵ鷯 ���
	private void handleBtnImageFileAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		selectFile = fileChooser.showOpenDialog(stage);// ���õ� ������ ������.

		try {
			if (selectFile != null) {
				String localURL = selectFile.toURI().toURL().toString();// ������ ���� ��ΰ� ���ڿ��� ��ȯ�ȴ�.
				Image image = new Image(localURL, false);
				imgView.setImage(image);
			}
		} catch (MalformedURLException e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("�̹��� �ε� ����");
			alert.setHeaderText("Ȯ�� �ٶ��ϴ�.");
			alert.setContentText("����!");
			alert.showAndWait();
		}
	}

	// �̹��� ���� ���
	private void setDefaultImageView() {
		Image image = new Image("/images/default.jpg", false);
		imgView.setImage(image);
	}

	// ����Ʈ ��ư �̺�Ʈ �ڵ鷯 ���
	private void handleBtnListAction(ActionEvent event) {
		obsList.clear(); // �Ű������� �ڽ��� �ָ� �ڱⰡ ������
		totalLoadList();
	}

	// mysql ����̹� �ε� : studentDB ������ ���̽����� gradeTBL���� ��ü�� �������� �̺�Ʈ
	private void totalLoadList() {
		StudentDAO studentDAO = new StudentDAO();
		ArrayList<Student> arrayList = studentDAO.getTotalLoadList();

		if (arrayList == null)
			return;

		// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
		for (int i = 0; i < arrayList.size(); i++) {
			Student s = arrayList.get(i);
			obsList.add(s);
		}
		tableView.setItems(obsList);
	}

	// ������Ʈ �̺�Ʈ ��� �ڵ鷯 �Լ� ó��(����Ŭ���ϸ� �߻�)
	private void handlePieChartAction(MouseEvent event) {// event�� �̺�Ʈ�� �߻��� ��� ������ �Ѿ��
		//
		if (event.getClickCount() != 2)
			return;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/pichart.fxml"));
			Scene scene = new Scene(root);
			Stage pieChartStage = new Stage(StageStyle.UTILITY);

			// �̺�Ʈ ��� �� ó��
			PieChart pieChart = (PieChart) scene.lookup("#pieChart");
			Button btnClose = (Button) scene.lookup("#btnClose");

			// 2�� Ŭ���� �л� ��ü�� ������
			Student student = obsList.get(tableViewSelectedIndex);

			// ��Ʈ�� �Է��� ������ observableList�� �Է�
			ObservableList pieChartObsList = FXCollections.observableArrayList();

			//
			pieChartObsList.add(new PieChart.Data("����", Integer.parseInt(student.getKorean())));
			pieChartObsList.add(new PieChart.Data("����", Integer.parseInt(student.getEnglish())));
			pieChartObsList.add(new PieChart.Data("����", Integer.parseInt(student.getMath())));
			pieChartObsList.add(new PieChart.Data("��ȸ", Integer.parseInt(student.getSoc())));
			pieChartObsList.add(new PieChart.Data("����", Integer.parseInt(student.getSic())));
			pieChartObsList.add(new PieChart.Data("����", Integer.parseInt(student.getMusic())));
			pieChart.setData(pieChartObsList);

			pieChartStage.initModality(Modality.WINDOW_MODAL);
			pieChartStage.initOwner(stage);
			pieChartStage.setScene(scene);
			pieChartStage.setTitle("���� ���� ��Ʈ ���� : ���� ��Ʈ");
			pieChartStage.show();

			btnClose.setOnAction(event1 -> pieChartStage.close());
		} catch (IOException e) {
		}
	}

	// �ʱⰪ ���� �Լ�
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

	// ��Ʈ ��ư �̺�Ʈ �ڵ鷯 ���
	private void handleBtnChartAction(ActionEvent event) {
		// ���� �����ͼ�, stage scene ����
		try {
			if (obsList.size() == 0)
				throw new Exception();

			Parent root = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Scene scnen = new Scene(root);
			Stage barStage = new Stage(StageStyle.UTILITY);

			// �̺�Ʈ �ڵ鷯 ���
			BarChart barChart = (BarChart) scnen.lookup("#barChart");
			Button btnClose = (Button) scnen.lookup("#btnClose");

			// ���� ��Ʈ�� XYchart �ø�� �����. (����)
			XYChart.Series seriesKorean = new XYChart.Series();
			seriesKorean.setName("����");
			ObservableList koreanList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				koreanList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesKorean.setData(koreanList);
			barChart.getData().add(seriesKorean);

			// ���� ��Ʈ�� XYchart �ø�� �����. (����)
			XYChart.Series seriesEnglish = new XYChart.Series();
			seriesEnglish.setName("����");
			ObservableList englishList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				englishList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesEnglish.setData(englishList);
			barChart.getData().add(seriesEnglish);

			// ���� ��Ʈ�� XYchart �ø�� �����. (����)
			XYChart.Series seriesMath = new XYChart.Series();
			seriesMath.setName("����");
			ObservableList mathList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				mathList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesMath.setData(mathList);
			barChart.getData().add(seriesMath);

			// ���� ��Ʈ�� XYchart �ø�� �����. (��ȸ)
			XYChart.Series seriesScience = new XYChart.Series();
			seriesScience.setName("����");
			ObservableList scienceList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				scienceList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesScience.setData(scienceList);
			barChart.getData().add(seriesScience);

			// ���� ��Ʈ�� XYchart �ø�� �����. (��ȸ)
			XYChart.Series seriesSociety = new XYChart.Series();
			seriesSociety.setName("����");
			ObservableList societyList = FXCollections.observableArrayList();
			for (int i = 0; i < obsList.size(); i++) {
				Student student = obsList.get(i);
				societyList.add(new XYChart.Data(student.getName(), Integer.parseInt(student.getKorean())));
			}
			seriesSociety.setData(societyList);
			barChart.getData().add(seriesSociety);

			// ���� ��Ʈ�� XYchart �ø�� �����. (����)
			XYChart.Series seriesMusic = new XYChart.Series();
			seriesMusic.setName("����");
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
			barStage.setTitle("���� ���� �׷���");
			barStage.show();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("������ ����Ʈ�� �����ϴ�.");
			alert.setHeaderText("tableView ����Ʈ �Է¿��");
			alert.setContentText("����!");
			alert.showAndWait();
		}

	}

	// ���� �Է�â�� 3�ڸ� ������ �Է� 0~100������ ������ �Է� �� �� �ֵ��� ����
	private void textFieldNumberFormat() {
		DecimalFormat decimalFormat = new DecimalFormat("###"); // ������ 3�ڸ������� �ްڴ�!. decimal�� ������ ��� ��, ### 3�� 3�ڸ��� �ǹ�
		txtKo.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0�ָ� if������ ���ڴ� 100���� Ŀ�� �ϴµ� ��ũ�� ������ ��� �ȵ�
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("�����Է� ���");
				alert.setHeaderText("����(0~100)�� �Է��Ͻʽÿ�");
				alert.setContentText("���� �� �Է� �Ұ�!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));
		txtEng.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0�ָ� if������ ���ڴ� 100���� Ŀ�� �ϴµ� ��ũ�� ������ ��� �ȵ�
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("�����Է� ���");
				alert.setHeaderText("����(0~100)�� �Է��Ͻʽÿ�");
				alert.setContentText("���� �� �Է� �Ұ�!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));
		txtMath.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0�ָ� if������ ���ڴ� 100���� Ŀ�� �ϴµ� ��ũ�� ������ ��� �ȵ�
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("�����Է� ���");
				alert.setHeaderText("����(0~100)�� �Է��Ͻʽÿ�");
				alert.setContentText("���� �� �Է� �Ұ�!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));
		txtSic.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0�ָ� if������ ���ڴ� 100���� Ŀ�� �ϴµ� ��ũ�� ������ ��� �ȵ�
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("�����Է� ���");
				alert.setHeaderText("����(0~100)�� �Է��Ͻʽÿ�");
				alert.setContentText("���� �� �Է� �Ұ�!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));
		txtSoc.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0�ָ� if������ ���ڴ� 100���� Ŀ�� �ϴµ� ��ũ�� ������ ��� �ȵ�
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("�����Է� ���");
				alert.setHeaderText("����(0~100)�� �Է��Ͻʽÿ�");
				alert.setContentText("���� �� �Է� �Ұ�!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));
		txtMusic.setTextFormatter(new TextFormatter<>(e -> {
			// ���ڸ� �Է��ؼ� �� '����'�̸� �ٽ� �̺�Ʈ�� ������
			if (e.getControlNewText().isEmpty()) {
				return e;
			}
			// ��ġ����( Ű���� ġ�� ��ġ�� �����Ѵ�.)
			ParsePosition parsePosition = new ParsePosition(0);
			// ���ڸ� �ްڴ�.
			Object object = decimalFormat.parse(e.getControlNewText(), parsePosition);
			int number = Integer.MAX_VALUE; // 0�ָ� if������ ���ڴ� 100���� Ŀ�� �ϴµ� ��ũ�� ������ ��� �ȵ�
			try {
				number = Integer.parseInt(e.getControlNewText());
			} catch (NumberFormatException e2) {
				Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
				alert.setTitle("�����Է� ���");
				alert.setHeaderText("����(0~100)�� �Է��Ͻʽÿ�");
				alert.setContentText("���� �� �Է� �Ұ�!");
				alert.showAndWait();
			}

			if (object == null || e.getControlNewText().length() >= 4 || number > 100) {
				return null; // �Է��� ���� �ȹްڴ�. ���ڰ� �ƴϸ� �ȹްڴ�. 3�ڸ� ���ڸ� �ްڴ�.
			} else {
				return e;
			}
		}));

	}

	// ���� ��ư �̺�Ʈ �ڵ鷯 ���
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

			// tebleView ���� ���õ� ��ġ���� ������ observableList���� �� ��ġ�� ã�Ƽ� �ش� Student ��ü�� �������� ��.
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

			// txtNo �ؽ�Ʈ �ʵ带 readOnly �� �����. (�б⸸ ������ ����)(��ȣ,�̸�,�г�, ��)
			txtNo.setDisable(true);
			txtName.setDisable(true);
			txtYear.setDisable(true);
			txtBan.setDisable(true);
			txtGender.setDisable(true);

			Scene scene = new Scene(root);
			Stage editStage = new javafx.stage.Stage(StageStyle.UTILITY); // �ӽð�ü���� ����� ���� fianl�� ��.

			// ����â�� �ش�� �̺�Ʈ ��� �� �ڵ鷯 ó�� ���
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
					Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
					alert.setTitle("�� �Է� ���");
					alert.setHeaderText("�Է� ���� ���� ���� �ֽ��ϴ�.");
					alert.setContentText("�����ϼ���!");
					alert.showAndWait();
				}
			});

			// ����â�� �ش�� �̺�Ʈ ��� �� �ڵ鷯 ó�� ���
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

				// mysql ����̹� �ε�
				StudentDAO studentDAO = new StudentDAO();
				int returnValue = studentDAO.getStudentUpdate(student1);

				if (returnValue != 0)
					obsList.set(tableViewSelectedIndex, student1); // tableView obsList�� �ش�� ��ġ�� ������ ��ü�� ����
				// observableList�� ���õ� ��ġ�� ������ ��üStudent ���� �־���.
				editStage.close();
			});

			// stage ���� �� ����
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(stage);
			editStage.setTitle("����");
			editStage.setScene(scene);
			editStage.setResizable(false);
			editStage.show();
			// ��� ��ư ����� â �ݱ�
			btnFormCancel.setOnAction((ActionEvent event1) -> editStage.close());

		} catch (IndexOutOfBoundsException | IOException e) { // �ѹ��� ���� 2�� �ֱ�
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("���� ����");
			alert.setHeaderText("������ ������ �����ϴ�.");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
		}
	}

	// tableView �������� �� �̺�Ʈ ���
	private void handleTableViewPressedAction(MouseEvent event) {
		tableViewSelectedIndex = tableView.getSelectionModel().getSelectedIndex();
	}

	// ���̺� ���� ��ư
	private void handleBtnDeleteAction(ActionEvent event) {
		StudentDAO studentDAO = new StudentDAO();
		// ?�� ������ ���������� �ش�� �������� no ��ȣ�� ����.
		Student student = obsList.get(tableViewSelectedIndex);
		int no = student.getNo();
		int returnValue = studentDAO.getStudentDelete(no);

		if (returnValue != 0) {
			// �̹��� �������� ��������� �� ������ �����ؾߵȴ�.
			// 1. ������ �̹��� ���ϸ��� ã�´�.
			String fileName = student.getFileName();// ��� �̹��� �� �� ����, �׷��� ��θ� ã�ƾ���, directorySave�� ��ΰ� ����
			File fileDelete = new File(directorySave.getAbsolutePath() + "\\" + fileName);

			// ��θ� �� �����Դ��� Ȯ��, ���� �װ� �̹��� �������� Ȯ��
			if (fileDelete.exists() && fileDelete.isFile()) {
				fileDelete.delete();
			}
			obsList.remove(tableViewSelectedIndex);
		}
	}

	// ã�� ��ư�� ������ �˻��Ǵ� �̺�Ʈ
	private void handleBtnSearchAction(ActionEvent event) {

		// ���̺�信 ����ִ� ������ ���� obsList
		try {
			StudentDAO studentDAO = new StudentDAO();

			if (txtSearch.getText().trim().equals(""))
				throw new Exception();
			ArrayList<Student> arrayList = studentDAO.getStudentFind(txtSearch.getText().trim());

			// arrayList�� �ִ� ���� obsList�� �Է��Ѵ�.
			if (arrayList.size() != 0) {
				obsList.clear();
				for (int i = 0; i < arrayList.size(); i++) {
					Student s = arrayList.get(i);
					obsList.add(s);
				}
			}
			tableView.setItems(obsList);

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("�̸� �Է� ���");
			alert.setHeaderText("�̸��� �Է��Ͻʽÿ�");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
		}
	}

	// ���� ���� ��ư �׷� �ʱ�ȭ ó��
	private void radioButtonGenderInitialze() {
		group = new ToggleGroup();
		rdoMale.setToggleGroup(group);
		rdoFemale.setToggleGroup(group);
//		rdoMale.setSelected(true);

	}

	// �г��� �Է��ϴ� �ʱ�ȭ ó��
	private void comboBoxLevelInitialize() {
		ObservableList<String> obsList = FXCollections.observableArrayList();
		obsList.addAll("1", "2", "3", "4", "5", "6");
		cmbLevel.setItems(obsList);
	}

	// ��Ϲ�ư �̺�Ʈ ��� �ڵ鷯 �Լ�

	private void handleBtnOkAction(ActionEvent event) throws NullPointerException {
		StudentDAO studentDAO = new StudentDAO();

		// �̹��� ���� ó�� ���� 1. �̹��� ���ϸ��� �����ؼ� ����, �ش� ���丮�� ����
		// 1. �̹��� ���ϸ� ����

		if (selectFile == null) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("���� ���� ���");
			alert.setHeaderText("����(�̹��� ����)�� �����Ͻʽÿ�");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
			return;
		}
		// 2. ���� ������ �����ͼ� ���θ��� �̹��� ���ϸ� ����.
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String fileName = null;
		try {
			fileName = "stu" + System.currentTimeMillis() + selectFile.getName();

			// �̹��� ������ ����Ʈ ��Ʈ���� �ٲپ ���۸� ���
			// c����̺꿡, images/stu1231231Ȧ�浿.jpg�� ���� �Ϸ���
			bis = new BufferedInputStream(new FileInputStream(selectFile));
			bos = new BufferedOutputStream(new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));

			// �̹��� ������ �о ������ġ�� �ִ� ���Ͽ��ٰ� ����.
			// -1�� ���̻� ������ ���ٴ� ��
			int data = -1; // ������ �о �߸� ������ -1�� ������
			while ((data = bis.read()) != -1) { // �ѹ� �����ϸ� 10000���� ���ư�, �ѹ���Ʈ�� ���縦 ��.
				bos.write(data);
				bos.flush(); // ���ۿ� �ִ� ���� �� �����ϱ� ���� ������.
			}
		} catch (Exception e) {
			System.out.println("���� ���� ����" + e.getMessage());
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
				System.out.println("��¥�� �Է����ּ���.");
				return;
			}
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("��� �Է� ���");
			alert.setHeaderText("��¥�� �����ϼ���");
			alert.setContentText("��¥�� ���õ��� �ʾҽ��ϴ�.");
			alert.showAndWait();
		}
		Student s = null;
		// ?�� ������ ���������� �ش�� �������� no ��ȣ�� ����.
		try {
			s = new Student(txtName.getText(), cmbLevel.getSelectionModel().getSelectedItem().toString(),
					txtBan.getText(), ((RadioButton) group.getSelectedToggle()).getText(), txtKo.getText(),
					txtEng.getText(), txtMath.getText(), txtSic.getText(), txtSoc.getText(), txtMusic.getText(),
					txtTotal.getText(), txtAvg.getText(), dpkDate.getValue().toString(), fileName);
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("��� �Է� ���");
			alert.setHeaderText("��� �Է� �Ͻʽÿ�");
			alert.setContentText("�Էµ��� ���� ���� �ֽ��ϴ�. \n Ȯ�� �ٶ��ϴ�.");
			alert.showAndWait();
		}
		int returnValue = studentDAO.getStudentRegistry(s);
		if (returnValue != 0) {
			obsList.clear();
			totalLoadList();
			setDefaultImageView();
		}
	}

	// �ʱ�ȭ��ư �̺�Ʈ ��� �ڵ鷯 �Լ�
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

	// ��� �̺�Ʈ ��� �ڵ鷯�Լ� ó��
	private void handleBtnAvgAction(ActionEvent event) {
		try {
			double avg = Integer.parseInt(txtTotal.getText()) / 6.0;
			txtAvg.setText(String.format("%.1f", avg));
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("���� �Է� ���");
			alert.setHeaderText("������ �Է��Ͻʽÿ�");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
		}
	}

	// ���� �̺�Ʈ ��� �ڵ鷯�Լ�ó��
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
			Alert alert = new Alert(AlertType.ERROR); // stage, scene, ȭ�鳻���� alert�ȿ� �� �������()����ȭ �Ǿ�����
			alert.setTitle("�����Է� ���");
			alert.setHeaderText("������ �Է��Ͻʽÿ�");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
		}
	}

	// ���̺�� UI��ü �÷� �ʱ�ȭ ����(�÷� 11�� ����� Student �ʵ�� ����) - ���ȭ�Ѱ�
	private void tableViewColumnInitialize() {
		TableColumn colNo = new TableColumn("No");
		colNo.setMaxWidth(30);
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colName = new TableColumn("����");
		colName.setMaxWidth(70);
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colLevel = new TableColumn("�г�");
		colLevel.setMaxWidth(40);
		colLevel.setCellValueFactory(new PropertyValueFactory("level"));

		TableColumn colBan = new TableColumn("��");
		colBan.setMaxWidth(30);
		colBan.setCellValueFactory(new PropertyValueFactory<>("ban"));

		TableColumn colGender = new TableColumn("����");
		colGender.setMaxWidth(40);
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

		TableColumn colKorean = new TableColumn("����");
		colKorean.setMaxWidth(39);
		colKorean.setCellValueFactory(new PropertyValueFactory<>("korean"));

		TableColumn colEnglish = new TableColumn("����");
		colEnglish.setMaxWidth(39);
		colEnglish.setCellValueFactory(new PropertyValueFactory<>("english"));

		TableColumn colMath = new TableColumn("����");
		colMath.setMaxWidth(39);
		colMath.setCellValueFactory(new PropertyValueFactory<>("math"));

		TableColumn colSic = new TableColumn("����");
		colSic.setMaxWidth(39);
		colSic.setCellValueFactory(new PropertyValueFactory<>("sic"));

		TableColumn colSoc = new TableColumn("��ȸ");
		colSoc.setMaxWidth(39);
		colSoc.setCellValueFactory(new PropertyValueFactory<>("soc"));

		TableColumn colMusic = new TableColumn("����");
		colMusic.setMaxWidth(39);
		colMusic.setCellValueFactory(new PropertyValueFactory<>("music"));

		TableColumn colTotal = new TableColumn("����");
		colTotal.setMaxWidth(50);
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

		TableColumn colAvg = new TableColumn("���");
		colAvg.setMaxWidth(70);
		colAvg.setCellValueFactory(new PropertyValueFactory<>("avg"));

		TableColumn colRegister = new TableColumn("�����");
		colRegister.setMaxWidth(90);
		colRegister.setCellValueFactory(new PropertyValueFactory<>("register"));

		TableColumn colFileName = new TableColumn("�̹��� ���");
		colFileName.setMaxWidth(260);
		colFileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		
		tableView.getColumns().addAll(colNo, colName, colLevel, colBan, colGender, colKorean, colEnglish, colMath,
				colSic, colSoc, colMusic, colTotal, colAvg, colRegister, colFileName);
	}
}
