/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evoteproject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hp user
 */
public class index extends javax.swing.JFrame {

    //connection
    Connection connect = new koneksi().connection();

    String nama, jurusan, fakultas, visi, misi, foto;
    int id;

    int nim;

    private DefaultTableModel tabmode;

    private JFileChooser openfile;
    private BufferedImage buffer;

    /**
     * Creates new form index
     */
    public index() {
        initComponents();
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setTitle("E-VOTE");

        //TULIS KODINGAN DESIGN DISINI YA GUYS
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("image/admin.png").getImage().getScaledInstance(jLabel19.getHeight(), jLabel19.getWidth(), Image.SCALE_SMOOTH));
        jLabel19.setIcon(imageIcon);

    }

    private void clearText() {
        tfNama.setText("");
        tfMisi.setText("");
        tfVisi.setText("");
        tfJurusan.setSelectedIndex(0);
        jFotoKet.setText("Pilih file..");
    }

    private void moveTo(Component tujuan) {
        panelUtama.removeAll();
        panelUtama.repaint();

        panelUtama.add(tujuan);

        panelUtama.repaint();
        panelUtama.revalidate();
    }

    private void getCalon(int id) {
        String sqlcalon = "SELECT * FROM calon WHERE id=" + id;

        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sqlcalon);

            int i = 0;
            while (rs.next()) {
                nama = rs.getString("nama");
                jurusan = rs.getString("jurusan");
                visi = rs.getString("visi");
                misi = rs.getString("misi");
                foto = rs.getString("foto");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void showCalon() {

        String sqlcalon = "SELECT * FROM calon";

        String[] nama = new String[3];
        String[] foto = new String[3];

        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sqlcalon);

            int i = 0;
            while (rs.next()) {
                nama[i] = rs.getString("nama");
                foto[i] = rs.getString("foto");
                i++;
            }

            nama1.setText(nama[0]);
            nama2.setText(nama[1]);
            nama3.setText(nama[2]);

            ImageIcon imageIcon = new ImageIcon(new ImageIcon(foto[0]).getImage().getScaledInstance(foto1.getHeight(), foto1.getWidth(), Image.SCALE_SMOOTH));
            foto1.setIcon(imageIcon);
            ImageIcon imageIcon1 = new ImageIcon(new ImageIcon(foto[1]).getImage().getScaledInstance(foto1.getHeight(), foto1.getWidth(), Image.SCALE_SMOOTH));
            foto2.setIcon(imageIcon1);
            ImageIcon imageIcon2 = new ImageIcon(new ImageIcon(foto[2]).getImage().getScaledInstance(foto1.getHeight(), foto1.getWidth(), Image.SCALE_SMOOTH));
            foto3.setIcon(imageIcon2);

        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    private void tampil() {
        Object[] baris = {"No", "Nama", "Jurusan", "Visi", "Misi", "Jumlah Vote"};
        tabmode = new DefaultTableModel(null, baris);
        tabeladmin.setModel(tabmode);
        tabeladmin.setRowHeight(40);
        String sqlcalon = "SELECT * FROM calon";

        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sqlcalon);

            while (rs.next()) {

                String id = rs.getString("id");
                String nama = rs.getString("nama");
                String jurusan = rs.getString("jurusan");
                String visi = rs.getString("visi");
                String misi = rs.getString("misi");
                String hasil = rs.getString("hasil");
                String[] data = {id, nama, jurusan, visi, misi, hasil};
                tabmode.addRow(data);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void tambah() {

        String cek = "SELECT * FROM calon";
        int counter = 1;
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(cek);
            while (rs.next()) {
                if (counter >= 3) {
                    JOptionPane.showMessageDialog(null, "Calon sudah maksimal");
                    tampil();
                    return;
                }
                counter++;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        String nama = tfNama.getText();
        String jurusan = tfJurusan.getSelectedItem().toString();
        String foto = this.foto;
        String visi = tfVisi.getText();
        String misi = tfMisi.getText();
        String sql = "INSERT INTO calon VALUES('" + counter + "', '" + nama + "', '" + jurusan + "', '" + visi + "', '" + misi + "','" + foto + "',0)";
        try {
            Statement st = connect.createStatement();
            st.executeUpdate(sql);
            this.clearText();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    private void ubah() {

        String nama = tfNama.getText();
        String jurusan = tfJurusan.getSelectedItem().toString();
        String visi = tfVisi.getText();
        String misi = tfMisi.getText();
        String foto = this.foto;

        Statement st;
        try {
            st = connect.createStatement();
            st.executeUpdate("UPDATE calon SET nama = '" + nama + "',jurusan = '" + jurusan + "'"
                    + ",visi = '" + visi + "',misi = '" + misi + "',foto = '" + foto + "' WHERE id = " + this.id);
            this.clearText();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void delete() {
        try {
            Statement st = connect.createStatement();
            st.executeUpdate("DELETE FROM calon WHERE id ='" + this.id + "'");
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        String sql = "SELECT * FROM calon";

        int counter = 1;
        try {
            Statement st1 = connect.createStatement();
            ResultSet rs = st1.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                Statement st2 = connect.createStatement();
                st2.executeUpdate("UPDATE calon SET id='" + counter + "'WHERE id ='" + id + "' ");
                counter++;
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    private void OpenDir() {

        openfile = new JFileChooser();
        File dir = openfile.getSelectedFile();
        openfile.setCurrentDirectory(dir);
        openfile.setFileFilter(new FileNameExtensionFilter("JPG Files", "jpg"));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelUtama = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnAdmin = new javax.swing.JButton();
        loginAdmin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfPass = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        panelAdmin = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabeladmin = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        home1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLayeredPane10 = new javax.swing.JLayeredPane();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        tfVisi = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jLabel7 = new javax.swing.JLabel();
        tfJurusan = new javax.swing.JComboBox<>();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        jLabel9 = new javax.swing.JLabel();
        tfMisi = new javax.swing.JTextArea();
        jLayeredPane9 = new javax.swing.JLayeredPane();
        jLabel16 = new javax.swing.JLabel();
        jFoto = new javax.swing.JButton();
        jFotoKet = new javax.swing.JLabel();
        jLayeredPane5 = new javax.swing.JLayeredPane();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        tfNama = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        loginPemilih = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tfNIM = new javax.swing.JTextField();
        jVote = new javax.swing.JButton();
        fHome = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        panelPemilih = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLayeredPane7 = new javax.swing.JLayeredPane();
        btn4 = new javax.swing.JButton();
        foto2 = new javax.swing.JLabel();
        nama2 = new javax.swing.JLabel();
        btn5 = new javax.swing.JButton();
        jLayeredPane6 = new javax.swing.JLayeredPane();
        btn2 = new javax.swing.JButton();
        foto1 = new javax.swing.JLabel();
        nama1 = new javax.swing.JLabel();
        btn3 = new javax.swing.JButton();
        jLayeredPane8 = new javax.swing.JLayeredPane();
        btn6 = new javax.swing.JButton();
        foto3 = new javax.swing.JLabel();
        nama3 = new javax.swing.JLabel();
        btn7 = new javax.swing.JButton();
        Thanks = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jDone = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        panelUtama.setLayout(new java.awt.CardLayout());

        home.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Montserrat Black", 1, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SELAMAT DATANG DI E-VOTE");

        jButton1.setBackground(new java.awt.Color(0, 0, 204));
        jButton1.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Vote");
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnAdmin.setBackground(new java.awt.Color(0, 0, 204));
        btnAdmin.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        btnAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnAdmin.setText("Admin");
        btnAdmin.setBorderPainted(false);
        btnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 892, Short.MAX_VALUE)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap(360, Short.MAX_VALUE)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                .addContainerGap(359, Short.MAX_VALUE))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap(265, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(292, Short.MAX_VALUE))
        );

        panelUtama.add(home, "card2");

        loginAdmin.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Montserrat SemiBold", 1, 12)); // NOI18N
        jLabel1.setText("Username");

        tfUser.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Montserrat SemiBold", 1, 12)); // NOI18N
        jLabel3.setText("Password");

        tfPass.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N

        btnLogin.setBackground(new java.awt.Color(0, 0, 204));
        btnLogin.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnHome.setBackground(new java.awt.Color(0, 0, 204));
        btnHome.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Montserrat Black", 1, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("SELAMAT DATANG DI E-VOTE");

        javax.swing.GroupLayout loginAdminLayout = new javax.swing.GroupLayout(loginAdmin);
        loginAdmin.setLayout(loginAdminLayout);
        loginAdminLayout.setHorizontalGroup(
            loginAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 892, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginAdminLayout.createSequentialGroup()
                .addContainerGap(343, Short.MAX_VALUE)
                .addGroup(loginAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfPass)
                    .addComponent(jLabel3)
                    .addComponent(tfUser)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addContainerGap(341, Short.MAX_VALUE))
        );
        loginAdminLayout.setVerticalGroup(
            loginAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginAdminLayout.createSequentialGroup()
                .addContainerGap(209, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfPass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(208, Short.MAX_VALUE))
        );

        panelUtama.add(loginAdmin, "card3");

        panelAdmin.setBackground(new java.awt.Color(255, 255, 255));

        tabeladmin.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        tabeladmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabeladmin.setMinimumSize(new java.awt.Dimension(1300, 800));
        tabeladmin.setName(""); // NOI18N
        tabeladmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabeladminMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabeladmin);

        jPanel1.setBackground(new java.awt.Color(0, 0, 204));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        home1.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        home1.setForeground(new java.awt.Color(0, 0, 204));
        home1.setText("Home");
        home1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                home1ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Hai");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/evoteproject/image/admin.png"))); // NOI18N
        jLabel19.setToolTipText("");

        jLabel20.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Admin");

        jLabel21.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Welcome");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(home1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(home1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        jLabel6.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("PANEL ADMIN");

        tfVisi.setColumns(20);
        tfVisi.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        tfVisi.setRows(5);

        jLabel8.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 12)); // NOI18N
        jLabel8.setText("Visi");

        jLayeredPane3.setLayer(tfVisi, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane3.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane3Layout = new javax.swing.GroupLayout(jLayeredPane3);
        jLayeredPane3.setLayout(jLayeredPane3Layout);
        jLayeredPane3Layout.setHorizontalGroup(
            jLayeredPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(tfVisi, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
                .addContainerGap())
        );
        jLayeredPane3Layout.setVerticalGroup(
            jLayeredPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfVisi, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 12)); // NOI18N
        jLabel7.setText("Jurusan");

        tfJurusan.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        tfJurusan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S1 Sistem Informasi", "S1 Teknik Informatika" }));

        jLayeredPane2.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(tfJurusan, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(tfJurusan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 12)); // NOI18N
        jLabel9.setText("Misi");

        tfMisi.setColumns(20);
        tfMisi.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        tfMisi.setRows(8);
        tfMisi.setTabSize(10);

        jLayeredPane4.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(tfMisi, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane4Layout = new javax.swing.GroupLayout(jLayeredPane4);
        jLayeredPane4.setLayout(jLayeredPane4Layout);
        jLayeredPane4Layout.setHorizontalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(tfMisi, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
                .addContainerGap())
        );
        jLayeredPane4Layout.setVerticalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfMisi, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel16.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 12)); // NOI18N
        jLabel16.setText("Foto");

        jFoto.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        jFoto.setText("File");
        jFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFotoActionPerformed(evt);
            }
        });

        jFotoKet.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        jFotoKet.setText("Pilih file..");

        jLayeredPane9.setLayer(jLabel16, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane9.setLayer(jFoto, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane9.setLayer(jFotoKet, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane9Layout = new javax.swing.GroupLayout(jLayeredPane9);
        jLayeredPane9.setLayout(jLayeredPane9Layout);
        jLayeredPane9Layout.setHorizontalGroup(
            jLayeredPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane9Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jLayeredPane9Layout.createSequentialGroup()
                        .addComponent(jFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFotoKet, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jLayeredPane9Layout.setVerticalGroup(
            jLayeredPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jLayeredPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFotoKet)))
        );

        jButton2.setBackground(new java.awt.Color(0, 0, 204));
        jButton2.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Hapus");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 204));
        jButton4.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 204));
        jButton3.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Insert");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLayeredPane5.setLayer(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane5.setLayer(jButton4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane5.setLayer(jButton3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane5Layout = new javax.swing.GroupLayout(jLayeredPane5);
        jLayeredPane5.setLayout(jLayeredPane5Layout);
        jLayeredPane5Layout.setHorizontalGroup(
            jLayeredPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jLayeredPane5Layout.setVerticalGroup(
            jLayeredPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tfNama.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        tfNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfNamaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 12)); // NOI18N
        jLabel5.setText("Nama");

        jLayeredPane1.setLayer(tfNama, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfNama)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfNama, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane10.setLayer(jLayeredPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane10.setLayer(jLayeredPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane10.setLayer(jLayeredPane4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane10.setLayer(jLayeredPane9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane10.setLayer(jLayeredPane5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane10.setLayer(jLayeredPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane10Layout = new javax.swing.GroupLayout(jLayeredPane10);
        jLayeredPane10.setLayout(jLayeredPane10Layout);
        jLayeredPane10Layout.setHorizontalGroup(
            jLayeredPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jLayeredPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLayeredPane9, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jLayeredPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane10Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane10Layout.setVerticalGroup(
            jLayeredPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane10Layout.createSequentialGroup()
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLayeredPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jLayeredPane10Layout.createSequentialGroup()
                        .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLayeredPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelAdminLayout = new javax.swing.GroupLayout(panelAdmin);
        panelAdmin.setLayout(panelAdminLayout);
        panelAdminLayout.setHorizontalGroup(
            panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAdminLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAdminLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelAdminLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLayeredPane10)))
                .addGap(15, 15, 15))
        );
        panelAdminLayout.setVerticalGroup(
            panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAdminLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelUtama.add(panelAdmin, "card4");

        loginPemilih.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Montserrat Medium", 1, 12)); // NOI18N
        jLabel10.setText("Jurusan");

        jLabel11.setFont(new java.awt.Font("Montserrat Medium", 1, 12)); // NOI18N
        jLabel11.setText("NIM");

        tfNIM.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N

        jVote.setBackground(new java.awt.Color(0, 0, 204));
        jVote.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        jVote.setForeground(new java.awt.Color(255, 255, 255));
        jVote.setText("Vote");
        jVote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVoteActionPerformed(evt);
            }
        });

        fHome.setBackground(new java.awt.Color(255, 255, 255));
        fHome.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 14)); // NOI18N
        fHome.setText("Home");
        fHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fHomeActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S1 Sistem Informasi", "S1 Teknik Informatika" }));

        jLabel12.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 36)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("SELAMAT DATANG DI E-VOTE");

        javax.swing.GroupLayout loginPemilihLayout = new javax.swing.GroupLayout(loginPemilih);
        loginPemilih.setLayout(loginPemilihLayout);
        loginPemilihLayout.setHorizontalGroup(
            loginPemilihLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPemilihLayout.createSequentialGroup()
                .addContainerGap(351, Short.MAX_VALUE)
                .addGroup(loginPemilihLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(tfNIM)
                    .addComponent(jLabel10)
                    .addComponent(fHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jVote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, 190, Short.MAX_VALUE))
                .addContainerGap(351, Short.MAX_VALUE))
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 892, Short.MAX_VALUE)
        );
        loginPemilihLayout.setVerticalGroup(
            loginPemilihLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPemilihLayout.createSequentialGroup()
                .addContainerGap(182, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfNIM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jVote, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(224, Short.MAX_VALUE))
        );

        panelUtama.add(loginPemilih, "card5");

        panelPemilih.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 36)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("KANDIDAT CALON E-VOTE");

        btn4.setBackground(new java.awt.Color(0, 0, 204));
        btn4.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 14)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 255, 255));
        btn4.setText("Pilih");
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });

        foto2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        nama2.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        nama2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nama2.setText("Nama Calon");
        nama2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                nama2AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btn5.setBackground(new java.awt.Color(255, 255, 255));
        btn5.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        btn5.setText("Detail");
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });

        jLayeredPane7.setLayer(btn4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane7.setLayer(foto2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane7.setLayer(nama2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane7.setLayer(btn5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane7Layout = new javax.swing.GroupLayout(jLayeredPane7);
        jLayeredPane7.setLayout(jLayeredPane7Layout);
        jLayeredPane7Layout.setHorizontalGroup(
            jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(foto2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(nama2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jLayeredPane7Layout.setVerticalGroup(
            jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nama2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foto2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn2.setBackground(new java.awt.Color(0, 0, 204));
        btn2.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 14)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 255, 255));
        btn2.setText("Pilih");
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });

        foto1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        nama1.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        nama1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nama1.setText("Nama Calon");
        nama1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                nama1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btn3.setBackground(new java.awt.Color(255, 255, 255));
        btn3.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        btn3.setText("Detail");
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        jLayeredPane6.setLayer(btn2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane6.setLayer(foto1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane6.setLayer(nama1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane6.setLayer(btn3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane6Layout = new javax.swing.GroupLayout(jLayeredPane6);
        jLayeredPane6.setLayout(jLayeredPane6Layout);
        jLayeredPane6Layout.setHorizontalGroup(
            jLayeredPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(foto1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                    .addComponent(nama1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jLayeredPane6Layout.setVerticalGroup(
            jLayeredPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nama1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foto1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn6.setBackground(new java.awt.Color(0, 0, 204));
        btn6.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 14)); // NOI18N
        btn6.setForeground(new java.awt.Color(255, 255, 255));
        btn6.setText("Pilih");
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });

        foto3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        nama3.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        nama3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nama3.setText("Nama Calon");
        nama3.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                nama3AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btn7.setBackground(new java.awt.Color(255, 255, 255));
        btn7.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        btn7.setText("Detail");
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });

        jLayeredPane8.setLayer(btn6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane8.setLayer(foto3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane8.setLayer(nama3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane8.setLayer(btn7, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane8Layout = new javax.swing.GroupLayout(jLayeredPane8);
        jLayeredPane8.setLayout(jLayeredPane8Layout);
        jLayeredPane8Layout.setHorizontalGroup(
            jLayeredPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nama3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(foto3, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
        );
        jLayeredPane8Layout.setVerticalGroup(
            jLayeredPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nama3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foto3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelPemilihLayout = new javax.swing.GroupLayout(panelPemilih);
        panelPemilih.setLayout(panelPemilihLayout);
        panelPemilihLayout.setHorizontalGroup(
            panelPemilihLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelPemilihLayout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(jLayeredPane6)
                .addGap(18, 18, 18)
                .addComponent(jLayeredPane7)
                .addGap(18, 18, 18)
                .addComponent(jLayeredPane8)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        panelPemilihLayout.setVerticalGroup(
            panelPemilihLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPemilihLayout.createSequentialGroup()
                .addContainerGap(149, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelPemilihLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLayeredPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLayeredPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLayeredPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(149, Short.MAX_VALUE))
        );

        panelUtama.add(panelPemilih, "card6");

        Thanks.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Montserrat Medium", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Data anda sudah diinput, silahkan menunggu hasil");
        jLabel13.setToolTipText("");

        jLabel15.setFont(new java.awt.Font("Montserrat Black", 1, 48)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("TERIMA KASIH");
        jLabel15.setToolTipText("");

        jDone.setBackground(new java.awt.Color(0, 0, 204));
        jDone.setFont(new java.awt.Font("Montserrat ExtraBold", 1, 18)); // NOI18N
        jDone.setForeground(new java.awt.Color(255, 255, 255));
        jDone.setText("Done");
        jDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ThanksLayout = new javax.swing.GroupLayout(Thanks);
        Thanks.setLayout(ThanksLayout);
        ThanksLayout.setHorizontalGroup(
            ThanksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 892, Short.MAX_VALUE)
            .addGroup(ThanksLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jDone, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ThanksLayout.setVerticalGroup(
            ThanksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThanksLayout.createSequentialGroup()
                .addContainerGap(255, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jDone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(296, Short.MAX_VALUE))
        );

        panelUtama.add(Thanks, "card7");

        getContentPane().add(panelUtama, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        moveTo(loginPemilih);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed
        moveTo(loginAdmin);
    }//GEN-LAST:event_btnAdminActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String user = tfUser.getText();
        String pass = String.valueOf(tfPass.getPassword());
        String query = "SELECT * FROM admin WHERE username='" + user + "' AND password ='" + pass + "'";
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            boolean indikator = false;
            while (rs.next()) {
                if ((rs.getString(1).equals(user)) && (rs.getString(2).equals(pass))) {
                    moveTo(panelAdmin);
                    tampil();
                    indikator = true;
                    tfVisi.setLineWrap(true);
                    tfVisi.setWrapStyleWord(true);
                    tfMisi.setLineWrap(true);
                    tfMisi.setWrapStyleWord(true);
                    tfUser.setText("");
                    tfPass.setText("");

                }
            }
            if (indikator == false) {
                JOptionPane.showMessageDialog(null, "Maaf, Username atau Password Salah");
                tfUser.setText("");
                tfPass.setText("");
            }
        } catch (SQLException ex) {
            System.out.println("Error SQL");
        }

    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        moveTo(home);
        tfUser.setText("");
        tfPass.setText("");
    }//GEN-LAST:event_btnHomeActionPerformed

    private void tabeladminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabeladminMouseClicked
        int i = tabeladmin.getSelectedRow();

        this.id = Integer.parseInt((String) tabmode.getValueAt(i, 0));

        tfNama.setText((String) tabmode.getValueAt(i, 1));
        tfJurusan.setSelectedItem((String) tabmode.getValueAt(i, 2));
        tfVisi.setText((String) tabmode.getValueAt(i, 3));
        tfMisi.setText((String) tabmode.getValueAt(i, 4));

        String query = "SELECT * FROM calon WHERE id = " + id;
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                foto = rs.getString("foto");
            }
        } catch (SQLException ex) {
            System.out.println("Error SQL");
        }

        jFotoKet.setText(foto);

        this.nama = tfNama.getText();
        this.jurusan = tfJurusan.getSelectedItem().toString();
        this.visi = tfVisi.getText();
        this.misi = tfMisi.getText();
    }//GEN-LAST:event_tabeladminMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ?");

        switch (jawab) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CANCEL_OPTION:
                return;
        }
        delete();
        tampil();
        clearText();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ?");

        switch (jawab) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CANCEL_OPTION:
                return;
        }
        tambah();
        tampil();
        clearText();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ?");

        switch (jawab) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CANCEL_OPTION:
                return;
        }
        ubah();
        tampil();
        clearText();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tfNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfNamaActionPerformed

    private void home1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_home1ActionPerformed
        moveTo(home);
    }//GEN-LAST:event_home1ActionPerformed

    private void jVoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jVoteActionPerformed
        String jurusan = jComboBox1.getSelectedItem().toString();
        nim = Integer.parseInt(tfNIM.getText());

        String sql = "SELECT * FROM pemilih WHERE nim ='" + nim + "' AND jurusan='" + jurusan + "' AND ket=1";
        Statement st;
        try {
            st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                moveTo(panelPemilih);
                tfNIM.setText("");
                showCalon();
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, NIM sudah digunakan");
                jComboBox1.setSelectedIndex(0);
                tfNIM.setText("");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jVoteActionPerformed

    private void fHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fHomeActionPerformed
        moveTo(home);
    }//GEN-LAST:event_fHomeActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ?");

        switch (jawab) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CANCEL_OPTION:
                return;
        }

        String sql = "SELECT * FROM calon WHERE id = 1";
        Statement st;
        int hasil = 0;
        try {
            st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                hasil = rs.getInt("hasil");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        hasil++;
        sql = "UPDATE calon SET hasil = '" + hasil + "' WHERE id = 1";
        try {
            st = connect.createStatement();
            st.executeUpdate(sql);

            try {
                st = connect.createStatement();
                st.executeUpdate("UPDATE pemilih SET ket = 0 WHERE nim = " + nim);
            } catch (SQLException ex) {
                System.out.println(ex);
            }

            moveTo(Thanks);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }//GEN-LAST:event_btn2ActionPerformed

    private void nama1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_nama1AncestorAdded

    }//GEN-LAST:event_nama1AncestorAdded

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        getCalon(1);
        details dt = new details(nama, jurusan, visi, misi, foto);
        dt.setVisible(true);
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ?");

        switch (jawab) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CANCEL_OPTION:
                return;
        }

        String sql = "SELECT * FROM calon WHERE id = 2";
        Statement st;
        int hasil = 0;
        try {
            st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                hasil = rs.getInt("hasil");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        hasil++;
        sql = "UPDATE calon SET hasil = '" + hasil + "' WHERE id = 2";
        try {
            st = connect.createStatement();
            st.executeUpdate(sql);

            try {
                st = connect.createStatement();
                st.executeUpdate("UPDATE pemilih SET ket = 0 WHERE nim = " + nim);
            } catch (SQLException ex) {
                System.out.println(ex);
            }

            moveTo(Thanks);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_btn4ActionPerformed

    private void nama2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_nama2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_nama2AncestorAdded

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        getCalon(2);
        details dt = new details(nama, jurusan, visi, misi, foto);
        dt.setVisible(true);
    }//GEN-LAST:event_btn5ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        int jawab = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ?");

        switch (jawab) {
            case JOptionPane.YES_OPTION:
                break;
            case JOptionPane.NO_OPTION:
                return;
            case JOptionPane.CANCEL_OPTION:
                return;
        }

        String sql = "SELECT * FROM calon WHERE id = 3";
        Statement st;
        int hasil = 0;
        try {
            st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                hasil = rs.getInt("hasil");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        hasil++;
        sql = "UPDATE calon SET hasil = '" + hasil + "' WHERE id = 3";
        try {
            st = connect.createStatement();
            st.executeUpdate(sql);

            try {
                st = connect.createStatement();
                st.executeUpdate("UPDATE pemilih SET ket = 0 WHERE nim = " + nim);
            } catch (SQLException ex) {
                System.out.println(ex);
            }

            moveTo(Thanks);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_btn6ActionPerformed

    private void nama3AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_nama3AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_nama3AncestorAdded

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        getCalon(3);
        details dt = new details(nama, jurusan, visi, misi, foto);
        dt.setVisible(true);
    }//GEN-LAST:event_btn7ActionPerformed

    private void jDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDoneActionPerformed
        moveTo(loginPemilih);
    }//GEN-LAST:event_jDoneActionPerformed

    private void jFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFotoActionPerformed
        this.OpenDir();

        int open = openfile.showOpenDialog(this);

        if (open == JFileChooser.APPROVE_OPTION) {

            try {
                buffer = ImageIO.read(openfile.getSelectedFile());

                String temp = String.valueOf(openfile.getSelectedFile());

                temp = temp.replace("\\", "/");

                jFotoKet.setText(temp);
                foto = temp;
                System.out.println(foto);
            } catch (IOException e) {
                System.out.println(e);
            }

        } else {
            jFotoKet.setText("Pilih file..");
        }
    }//GEN-LAST:event_jFotoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new index().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Thanks;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton fHome;
    private javax.swing.JLabel foto1;
    private javax.swing.JLabel foto2;
    private javax.swing.JLabel foto3;
    private javax.swing.JPanel home;
    private javax.swing.JButton home1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JButton jDone;
    private javax.swing.JButton jFoto;
    private javax.swing.JLabel jFotoKet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane10;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JLayeredPane jLayeredPane5;
    private javax.swing.JLayeredPane jLayeredPane6;
    private javax.swing.JLayeredPane jLayeredPane7;
    private javax.swing.JLayeredPane jLayeredPane8;
    private javax.swing.JLayeredPane jLayeredPane9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jVote;
    private javax.swing.JPanel loginAdmin;
    private javax.swing.JPanel loginPemilih;
    private javax.swing.JLabel nama1;
    private javax.swing.JLabel nama2;
    private javax.swing.JLabel nama3;
    private javax.swing.JPanel panelAdmin;
    private javax.swing.JPanel panelPemilih;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JTable tabeladmin;
    private javax.swing.JComboBox<String> tfJurusan;
    private javax.swing.JTextArea tfMisi;
    private javax.swing.JTextField tfNIM;
    private javax.swing.JTextField tfNama;
    private javax.swing.JPasswordField tfPass;
    private javax.swing.JTextField tfUser;
    private javax.swing.JTextArea tfVisi;
    // End of variables declaration//GEN-END:variables
}
