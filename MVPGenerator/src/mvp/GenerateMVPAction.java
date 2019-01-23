package mvp;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.impl.file.PsiDirectoryImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateMVPAction extends AnAction {
    private Project project;
    private PsiDirectoryImpl psiPackage;
    private String psiPath="",packageChoose = "";
    private JDialog jFrame;
    private JTextField name;
    private JTextField username;
    private String apiPath = "/data/api", datastorePath="/data/repository/datastore", mvpPath = "/mvp", presentationPath = "/presentation", diPath="/di";

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
        psiPackage = (PsiDirectoryImpl) e.getData(PlatformDataKeys.PSI_ELEMENT);
        psiPath = psiPackage.toString();
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1);
        packageChoose = psiPath.substring(psiPath.indexOf("/java/") + 1);
        packageChoose = packageChoose.substring(packageChoose.indexOf("/") + 1).replaceAll("/",".");
        initSelectView();
    }

    private void initSelectView() {
        jFrame = new JDialog();// 定义一个窗体Container container = getContentPane();
        jFrame.setModal(true);
        Container container = jFrame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel panelname = new JPanel();// /定义一个面板
        panelname.setLayout(new GridLayout(1, 2));
        panelname.setBorder(BorderFactory.createTitledBorder("命名"));

        name = new JTextField();
        name.setText("请输入模块名");
        panelname.add(name);

        username = new JTextField();
        username.setText("请输入注释的作者");
        panelname.add(username);

        container.add(panelname);

        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout());

        Button cancle = new Button();
        cancle.setLabel("取消");
        cancle.addActionListener(actionListener);

        Button ok = new Button();
        ok.setLabel("确定");
        ok.addActionListener(actionListener);
        menu.add(cancle);
        menu.add(ok);
        container.add(menu);

        jFrame.setSize(400, 200);
        jFrame.setLocationRelativeTo(null);

        jFrame.setVisible(true);
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("取消")) {
                jFrame.dispose();
            } else {
                jFrame.dispose();
                clickCreateFile();
                Messages.showInfoMessage(project,"生成完毕","提示");
            }
        }
    };

    private void clickCreateFile(){
        String nameFile = name.getText();
        String api = psiPath+apiPath;
        String datastore = psiPath+datastorePath;
        String mvp = psiPath + mvpPath;
        String presentation = psiPath+presentationPath;
        String repository = psiPath+"/data/repository";
        String di = psiPath + diPath;
        new File(api).mkdirs();
        new File(datastore).mkdirs();
        new File(mvp).mkdirs();
        new File(presentation).mkdirs();
        new File(di).mkdirs();

        generateFile("data/api/TApiService.txt", api, nameFile+"ApiService.java");
        generateFile("data/repository/datastore/TDataStore.txt",datastore, nameFile+"DataStore.java");
        generateFile("data/repository/datastore/TLocalDataStore.txt",datastore, nameFile+"LocalDataStore.java");
        generateFile("data/repository/datastore/TRemoteDataStore.txt",datastore, nameFile+"RemoteDataStore.java");
        generateFile("data/repository/ITRepository.txt",repository, "I"+nameFile+"Repository.java");
        generateFile("data/repository/TRepositoryImpl.txt",repository, nameFile+"RepositoryImpl.java");

        generateFile("mvp/TContract.txt",mvp,nameFile+"Contract.java");
        generateFile("mvp/TPresenter.txt",mvp,nameFile+"Presenter.java");

        generateFile("di/PerTScene.txt",di,"Per"+nameFile+"Scene.java");
        generateFile("di/TComponent.txt",di,nameFile+"Component.java");
        generateFile("di/TDataModule.txt",di,nameFile+"DataModule.java");
        generateFile("di/TPresenterModule.txt",di,nameFile+"PresenterModule.java");
    }

    private void generateFile(String srcFile, String filePath, String fileName){
        String content = readFile(srcFile);
        content = dealFile(content);
        writetoFile(content, filePath,fileName);
    }

    private String readFile(String filename){
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/Templates/"+filename);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (Exception e) {
        }
        return content;
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
                System.out.println(new String(buffer));
            }

        } catch (IOException e) {
        } finally {
            outSteam.close();
            inStream.close();
        }
        return outSteam.toByteArray();
    }

    private String dealFile(String content) {
        content = content.replaceAll("\\$author", username.getText());
        content = content.replaceAll("\\$packagename", packageChoose);
        content = content.replaceAll("\\$date", getNowDateShort());
        content = content.replaceAll("\\$name", name.getText());
        return content;
    }

    public String getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(currentTime);
    }

    private void writetoFile(String content, String filepath, String filename) {
        try {
            File floder = new File(filepath);
            // if file doesnt exists, then create it
            if (!floder.exists()) {
                floder.mkdirs();
            }
            File file = new File(filepath + "/" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
