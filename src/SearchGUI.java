import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class SearchGUI extends JFrame implements ActionListener {
    LuceneSearch tester = new LuceneSearch();
    JTextField textField;
    JLabel labelOut, label;
    JButton btn;
    DefaultListModel<String> listModel;
    JList<String> jList;
    JScrollPane scrollPane;

    SearchGUI() {
        label = new JLabel("Enter Query:");
        label.setBounds(50, 50, 100, 30);

        textField = new JTextField();
        textField.setBounds(150, 50, 150, 30);
        textField.setForeground(Color.DARK_GRAY);

        btn = new JButton("Search");
        btn.setBounds(50, 100, 250, 30);
        btn.addActionListener(this);

        labelOut = new JLabel();
        labelOut.setBounds(50, 150, 250, 30);
        labelOut.setForeground(Color.RED);
        labelOut.setFont(new Font(
                "JetBrains Mono", Font.BOLD, 12));

        listModel = new DefaultListModel<>();
        jList = new JList<>(listModel);

        scrollPane = new JScrollPane(jList);
        scrollPane.setBounds(50, 200, 250, 200);

        add(label);
        add(textField);
        add(labelOut);
        add(scrollPane);
        add(btn);
        setSize(400, 500);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String query = textField.getText();
            if (query.isEmpty()) {
                labelOut.setText("No Query Given.");
            } else {
                listModel.removeAllElements();
                TopDocs hits = tester.search(query);
                labelOut.setText(hits.totalHits + " documents found.");
                for (ScoreDoc scoreDoc : hits.scoreDocs) {
                    Document doc = new Searcher(tester.indexDir).getDocument(scoreDoc);
                    listModel.addElement(doc.get(LuceneConstants.FILE_NAME));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        new SearchGUI();
    }
}
