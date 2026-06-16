                                                                                                                                                                                                                                                                                                                            `````````````````````                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TodoPetalApp {
    public static void main(String[] args) {
        // Initialize the database connection pool
        DatabaseManager.init();

        // Set custom UI defaults for a modern look
        Style.applyTheme(Theme.AQUA_DREAM); // CHANGED: Set the new default theme
        UIManager.put("Panel.background", Style.BACKGROUND_COLOR);
        UIManager.put("Frame.background", Style.BACKGROUND_COLOR);
        UIManager.put("Dialog.background", Style.BACKGROUND_COLOR);
        UIManager.put("TextField.border", new EmptyBorder(10, 10, 10, 10));
        UIManager.put("PasswordField.border", new EmptyBorder(10, 10, 10, 10));
        UIManager.put("TextArea.border", new EmptyBorder(10, 10, 10, 10));
        UIManager.put("Button.font", Style.FONT_BUTTON);
        UIManager.put("Label.font", Style.FONT_LABEL);
        UIManager.put("CheckBox.font", Style.FONT_LABEL);
        UIManager.put("ComboBox.font", Style.FONT_FIELD);
        UIManager.put("Table.font", Style.FONT_LABEL);
        UIManager.put("TableHeader.font", Style.FONT_BUTTON);

        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}


// --- DATABASE AND UTILITIES ---

class DatabaseManager {
    // !!! IMPORTANT: CONFIGURE YOUR DATABASE CONNECTION HERE !!!
    private static final String DB_URL = "jdbc:mysql://localhost:3307/todofinal";
    private static final String DB_USER = "misriya"; // Replace with your MySQL username
    private static final String DB_PASS = "root"; // Replace with your MySQL password

    public static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            showError("MySQL JDBC Driver not found. Please add it to your project's classpath.");
            System.exit(1);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}

// --- THEME AND STYLES ---
enum Theme { AQUA_DREAM, LAVENDER_BLISS, FOREST_GREEN } // ADDED: New theme option

class Style {
    // These are now mutable and changed by applyTheme()
    public static Color PRIMARY_COLOR;
    public static Color ACCENT_COLOR;
    public static Color BACKGROUND_COLOR;
    public static Color CARD_BACKGROUND_COLOR;
    public static Color TEXT_PRIMARY_COLOR;
    public static Color TEXT_SECONDARY_COLOR;
    public static Color DANGER_COLOR;
    public static Color CHART_COLOR_1, CHART_COLOR_2, CHART_COLOR_3, CHART_COLOR_4;


    // Priority Colors (usually constant)
    public static final Color PRIORITY_HIGH = new Color(255, 77, 77);
    public static final Color PRIORITY_MEDIUM = new Color(255, 191, 0);
    public static final Color PRIORITY_LOW = new Color(60, 179, 113);

    // Status Colors
    public static final Color STATUS_TODO = new Color(0, 150, 255);
    public static final Color STATUS_IN_PROGRESS = new Color(255, 165, 0);
    public static final Color STATUS_DONE = new Color(60, 179, 113);

    // Fonts (constant)
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 42);
    public static final Font FONT_TAGLINE = new Font("Segoe UI Light", Font.ITALIC, 22);
    public static final Font FONT_FORM_HEADER = new Font("Segoe UI Semibold", Font.PLAIN, 32);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 18);
    public static final Font FONT_BUTTON = new Font("Segoe UI Semibold", Font.PLAIN, 18);
    public static final Font FONT_TASK_DESC = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_TASK_DETAILS = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI Semibold", Font.PLAIN, 18);
    public static final Font FONT_CARD_TITLE = new Font("Segoe UI Semibold", Font.PLAIN, 20);
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 36);


    // Initialize with default theme
    static {
        applyTheme(Theme.AQUA_DREAM); // CHANGED: Set the new default theme
    }

    public static void applyTheme(Theme theme) {
        switch (theme) {
            case AQUA_DREAM: // ADDED: New theme color palette
                PRIMARY_COLOR = new Color(0, 122, 122);
                ACCENT_COLOR = new Color(0, 180, 180);
                BACKGROUND_COLOR = new Color(225, 245, 245);
                CARD_BACKGROUND_COLOR = new Color(248, 252, 252); // A very light off-white
                TEXT_PRIMARY_COLOR = new Color(20, 50, 50);
                TEXT_SECONDARY_COLOR = new Color(100, 130, 130);
                DANGER_COLOR = new Color(200, 50, 50);
                CHART_COLOR_1 = new Color(0, 122, 122);
                CHART_COLOR_2 = new Color(0, 150, 150);
                CHART_COLOR_3 = new Color(60, 180, 180);
                CHART_COLOR_4 = new Color(130, 210, 210);
                break;
            case FOREST_GREEN:
                PRIMARY_COLOR = new Color(34, 87, 122);
                ACCENT_COLOR = new Color(87, 160, 134);
                BACKGROUND_COLOR = new Color(240, 247, 245);
                CARD_BACKGROUND_COLOR = Color.WHITE;
                TEXT_PRIMARY_COLOR = new Color(40, 40, 40);
                TEXT_SECONDARY_COLOR = new Color(128, 128, 128);
                DANGER_COLOR = new Color(210, 4, 45);
                CHART_COLOR_1 = new Color(56, 102, 65);
                CHART_COLOR_2 = new Color(87, 160, 134);
                CHART_COLOR_3 = new Color(128, 184, 155);
                CHART_COLOR_4 = new Color(199, 228, 216);
                break;
            case LAVENDER_BLISS:
            default:
                PRIMARY_COLOR = new Color(118, 77, 242);
                ACCENT_COLOR = new Color(230, 80, 150);
                BACKGROUND_COLOR = new Color(244, 241, 255);
                CARD_BACKGROUND_COLOR = Color.WHITE;
                TEXT_PRIMARY_COLOR = new Color(30, 30, 30);
                TEXT_SECONDARY_COLOR = new Color(150, 150, 150);
                DANGER_COLOR = new Color(220, 20, 60);
                CHART_COLOR_1 = new Color(98, 4, 255);
                CHART_COLOR_2 = new Color(158, 123, 255);
                CHART_COLOR_3 = new Color(198, 178, 255);
                CHART_COLOR_4 = new Color(230, 80, 150);
                break;
        }
    }
}

// --- CUSTOM ROUNDED COMPONENTS ---
class RoundedPanel extends JPanel {
    private final int cornerRadius; private Color borderColor = null;
    public RoundedPanel(int r) { super(); this.cornerRadius = r; setOpaque(false); }
    public void setBorderColor(Color c) { this.borderColor = c; repaint(); }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g); Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        if (borderColor != null) { g2.setColor(borderColor); g2.setStroke(new BasicStroke(2)); g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius); }
        g2.dispose();
    }
}
class RoundedButton extends JButton {
    public RoundedButton(String text) { super(text); setOpaque(false); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); setCursor(new Cursor(Cursor.HAND_CURSOR)); }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) g2.setColor(getBackground().darker());
        else if (getModel().isRollover()) g2.setColor(getBackground().brighter());
        else g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30)); g2.dispose();
        super.paintComponent(g);
    }
}

// --- DATA MODELS (now with IDs) ---
enum Priority { HIGH, MEDIUM, LOW }
enum Status { TODO, IN_PROGRESS, DONE }
enum Category { IDEA("💡"), FOOD("🍽️"), WORK("💼"), SPORT("🏃"), MUSIC("🎵"), OTHER("📌");
    final String icon; Category(String icon) { this.icon = icon; }
}

class Task {
    private int id; private int userId;
    private String description; private Status status; private Priority priority; private Category category; private LocalDate dueDate;
    private LocalDate creationDate;
    public Task(int id, int userId, String d, Priority p, Category c, LocalDate date, Status s, LocalDate creation) {
        this.id = id; this.userId = userId; this.description = d; this.priority = p; this.category = c; this.dueDate = date; this.status = s; this.creationDate = creation;
    }
    // Constructor for new tasks without an ID yet
    public Task(int userId, String d, Priority p, Category c, LocalDate date, Status s) {
        this(0, userId, d, p, c, date, s, LocalDate.now());
    }

    public int getId() { return id; } public int getUserId() { return userId; } public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
    public Status getStatus() { return status; } public void setStatus(Status s) { this.status = s; }
    public Priority getPriority() { return priority; } public void setPriority(Priority p) { this.priority = p; }
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate d) { this.dueDate = d; }
    public Category getCategory() { return category; } public void setCategory(Category c) { this.category = c; }
    public LocalDate getCreationDate() { return creationDate; } @Override public String toString() { return description; }
}
class User {
    private int id; private String username; private String password; private String role; private String email; private String phone;
    private LocalDateTime lastLoginTime;
    public User(int id, String u, String p, String r, String e, String ph, LocalDateTime lastLogin) {
        this.id = id; this.username = u; this.password = p; this.role = r; this.email = e; this.phone = ph; this.lastLoginTime = lastLogin;
    }
    // Constructor for new user
    public User(String u, String p, String r, String e, String ph) {
        this(0, u, p, r, e, ph, null);
    }
    public int getId() {return id;} public String getUsername() { return username; } public String getPassword() { return password; }
    public String getRole() { return role; } public void setRole(String r) { this.role = r; }
    public String getEmail() { return email; } public void setEmail(String e) { this.email = e; }
    public String getPhone() { return phone; } public void setPhone(String p) { this.phone = p; }
    public LocalDateTime getLastLoginTime() { return lastLoginTime; } public void setLastLoginTime(LocalDateTime t) { this.lastLoginTime = t; }
}

// --- DATA ACCESS OBJECTS (DAO) ---

class UserDAO {
    public Optional<User> getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Timestamp lastLogin = rs.getTimestamp("last_login_time");
                return Optional.of(new User(
                    rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("role"), rs.getString("email"), rs.getString("phone"),
                    lastLogin == null ? null : lastLogin.toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
        }
        return Optional.empty();
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO users(username, password, role, email, phone) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, PasswordUtils.hashPassword(user.getPassword()));
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
            return false;
        }
    }
    
    public void updateLastLogin(User user) {
        String sql = "UPDATE users SET last_login_time = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
        }
    }
    
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                 Timestamp lastLogin = rs.getTimestamp("last_login_time");
                userList.add(new User(
                    rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("role"), rs.getString("email"), rs.getString("phone"),
                    lastLogin == null ? null : lastLogin.toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
        }
        return userList;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
            return false;
        }
    }
}

class TaskDAO {
    public List<Task> getTasksForUser(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(new Task(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getString("description"),
                    Priority.valueOf(rs.getString("priority")), Category.valueOf(rs.getString("category")),
                    rs.getDate("due_date").toLocalDate(), Status.valueOf(rs.getString("status")),
                    rs.getDate("creation_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
        }
        return tasks;
    }

    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks(user_id, description, status, priority, category, due_date, creation_date) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getUserId());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus().name());
            pstmt.setString(4, task.getPriority().name());
            pstmt.setString(5, task.getCategory().name());
            pstmt.setDate(6, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.setDate(7, java.sql.Date.valueOf(task.getCreationDate()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
            return false;
        }
    }

    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET description = ?, status = ?, priority = ?, category = ?, due_date = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getDescription());
            pstmt.setString(2, task.getStatus().name());
            pstmt.setString(3, task.getPriority().name());
            pstmt.setString(4, task.getCategory().name());
            pstmt.setDate(5, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.setInt(6, task.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
            return false;
        }
    }

    public Map<String, Long> getTaskCountByStatus(int userId) {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("TODO", 0L);
        counts.put("IN_PROGRESS", 0L);
        counts.put("DONE", 0L);

        String sql = "SELECT status, COUNT(*) as count FROM tasks WHERE user_id = ? GROUP BY status";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                counts.put(rs.getString("status"), rs.getLong("count"));
            }
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
        }
        return counts;
    }

    public Map<String, Integer> getCompletedTasksByDay(int userId, LocalDate start, LocalDate end) {
        Map<String, Integer> dailyData = new LinkedHashMap<>();
        String sql = "SELECT DATE(due_date) as completed_day, COUNT(*) as count " +
                     "FROM tasks WHERE user_id = ? AND status = 'DONE' AND due_date BETWEEN ? AND ? " +
                     "GROUP BY completed_day ORDER BY completed_day";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(start));
            pstmt.setDate(3, java.sql.Date.valueOf(end));
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                LocalDate day = rs.getDate("completed_day").toLocalDate();
                dailyData.put(day.format(DateTimeFormatter.ofPattern("dd MMM")), rs.getInt("count"));
            }
        } catch (SQLException e) {
            DatabaseManager.showError(e.getMessage());
        }
        return dailyData;
    }
}


// --- LOGIN AND SIGN-UP SCREENS (Updated for DAO) ---
class LoginPage extends JFrame {
    private final UserDAO userDAO = new UserDAO();

    public LoginPage() {
        setTitle("Todo Petal - Login"); setExtendedState(JFrame.MAXIMIZED_BOTH); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLayout(new BorderLayout());
        JSplitPane sp = new JSplitPane(); sp.setEnabled(false); sp.setDividerSize(0); sp.setResizeWeight(0.40); add(sp, BorderLayout.CENTER);
        sp.setLeftComponent(createBrandingPanel()); sp.setRightComponent(createFormPanel());
    }
    JPanel createBrandingPanel() {
        JPanel p = new JPanel(new GridBagLayout()); p.setBackground(Style.PRIMARY_COLOR); GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(10, 20, 10, 20);
        g.gridwidth = GridBagConstraints.REMAINDER; JLabel t = new JLabel("Todo Petal"); t.setFont(Style.FONT_HEADER); t.setForeground(Color.WHITE); p.add(t, g);
        JLabel s = new JLabel("Blossom into Productivity."); s.setFont(Style.FONT_TAGLINE); s.setForeground(new Color(230, 230, 255)); p.add(s, g); return p;
    }
    private JPanel createFormPanel() {
        JPanel p = new JPanel(new GridBagLayout()); p.setBackground(Style.CARD_BACKGROUND_COLOR); JPanel fc = new JPanel(new GridBagLayout()); fc.setBackground(Style.CARD_BACKGROUND_COLOR);
        GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(10, 5, 10, 5); g.fill = GridBagConstraints.HORIZONTAL; JLabel h = new JLabel("Welcome!");
        h.setFont(Style.FONT_FORM_HEADER); g.gridwidth = 2; g.weighty = 0.5; fc.add(h, g); g.weighty = 0; JTextField uf = addFormField(fc, g, "Username", 1);
        JPasswordField pf = addPasswordField(fc, g, "Password", 2); JComboBox<String> rb = addRoleField(fc, g, "Role", 3); g.gridy = 7; g.insets = new Insets(25, 5, 8, 5);
        JButton lb = new RoundedButton("Login"); lb.setBackground(Style.ACCENT_COLOR); lb.setForeground(Color.WHITE); lb.setFont(Style.FONT_BUTTON); fc.add(lb, g);
        g.gridy = 8; g.insets = new Insets(8, 5, 8, 5); JButton sb = new RoundedButton("Create Account"); sb.setBackground(Style.PRIMARY_COLOR);
        sb.setForeground(Color.WHITE); sb.setFont(Style.FONT_BUTTON); fc.add(sb, g);
        
        lb.addActionListener(e -> {
            String u = uf.getText(); String pass = new String(pf.getPassword()); String r = (String) rb.getSelectedItem();
            if (u.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "All fields required.", "Login Error", JOptionPane.ERROR_MESSAGE); return; }
            
            Optional<User> userOpt = userDAO.getUserByUsername(u);
            if (userOpt.isPresent() && PasswordUtils.checkPassword(pass, userOpt.get().getPassword()) && userOpt.get().getRole().equals(r)) {
                User user = userOpt.get();
                userDAO.updateLastLogin(user);
                dispose();
                if ("Admin".equals(r)) new AdminDashboard(this, user).setVisible(true); 
                else new UserDashboard(this, user).setVisible(true);
            } else { JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Error", JOptionPane.ERROR_MESSAGE); }
        });

        sb.addActionListener(e -> { dispose(); new SignUpPage().setVisible(true); }); p.add(fc); return p;
    }
    JTextField addFormField(JPanel p, GridBagConstraints g, String l, int y) {
        g.gridy = y * 2 - 1; p.add(new JLabel(l), g); JTextField f = new JTextField(25); f.setFont(Style.FONT_FIELD); g.gridy = y * 2; p.add(f, g); return f;
    }
    JPasswordField addPasswordField(JPanel p, GridBagConstraints g, String l, int y) {
        g.gridy = y * 2 - 1; p.add(new JLabel(l), g); JPasswordField f = new JPasswordField(25); f.setFont(Style.FONT_FIELD); g.gridy = y * 2; p.add(f, g); return f;
    }
    JComboBox<String> addRoleField(JPanel p, GridBagConstraints g, String l, int y) {
        g.gridy = y * 2 - 1; p.add(new JLabel(l), g); JComboBox<String> f = new JComboBox<>(new String[]{"User", "Admin"}); f.setFont(Style.FONT_FIELD); g.gridy = y * 2; p.add(f, g); return f;
    }
}

class SignUpPage extends JFrame {
    private final UserDAO userDAO = new UserDAO();
    public SignUpPage() {
        setTitle("Todo Petal - Create Account"); setExtendedState(JFrame.MAXIMIZED_BOTH); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLayout(new BorderLayout());
        JSplitPane sp = new JSplitPane(); sp.setEnabled(false); sp.setDividerSize(0); sp.setResizeWeight(0.40); add(sp, BorderLayout.CENTER);
        sp.setLeftComponent(new LoginPage().createBrandingPanel()); JPanel p = new JPanel(new GridBagLayout()); p.setBackground(Style.CARD_BACKGROUND_COLOR); // CHANGED
        JPanel fc = new JPanel(new GridBagLayout()); fc.setBackground(Style.CARD_BACKGROUND_COLOR); GridBagConstraints g = new GridBagConstraints(); // CHANGED
        g.insets = new Insets(8, 5, 8, 5); g.fill = GridBagConstraints.HORIZONTAL; JLabel h = new JLabel("Create an Account"); h.setFont(Style.FONT_FORM_HEADER);
        g.gridwidth = 2; g.weighty = 0.5; fc.add(h, g); g.weighty = 0; LoginPage lp = new LoginPage(); JTextField uf = lp.addFormField(fc, g, "Username", 1);
        JPasswordField pf = lp.addPasswordField(fc, g, "Password", 2); JTextField ef = lp.addFormField(fc, g, "Email", 3); JTextField phf = lp.addFormField(fc, g, "Phone Number", 4);
        JComboBox<String> rb = lp.addRoleField(fc, g, "Role", 5); g.insets = new Insets(20, 5, 8, 5); g.gridy = 11; JButton regb = new RoundedButton("Register");
        regb.setBackground(Style.ACCENT_COLOR); regb.setForeground(Color.WHITE); fc.add(regb, g); g.insets = new Insets(8, 5, 8, 5); g.gridy = 12;
        JButton backb = new RoundedButton("Back to Login"); backb.setBackground(Style.PRIMARY_COLOR); backb.setForeground(Color.WHITE); fc.add(backb, g);
        
        regb.addActionListener(e -> { 
            String u = uf.getText().trim(); String pass = new String(pf.getPassword()); String email = ef.getText().trim(); String phone = phf.getText().trim(); String role = (String) rb.getSelectedItem();
            if (u.isEmpty() || pass.isEmpty() || email.isEmpty() || phone.isEmpty()) { JOptionPane.showMessageDialog(this, "All fields required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email)) { JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (userDAO.getUserByUsername(u).isPresent()) { JOptionPane.showMessageDialog(this, "Username exists.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            
            User newUser = new User(u, pass, role, email, phone);
            if (userDAO.addUser(newUser)) {
                JOptionPane.showMessageDialog(this, "Account created!", "Success", JOptionPane.INFORMATION_MESSAGE); 
                dispose(); new LoginPage().setVisible(true);
            } else {
                 JOptionPane.showMessageDialog(this, "Could not create account. Check logs.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backb.addActionListener(e -> { dispose(); new LoginPage().setVisible(true); }); p.add(fc); sp.setRightComponent(p);
    }
}

// --- NEW DASHBOARD GRAPHICS COMPONENTS ---

class CircularProgressPanel extends JPanel {
    private double progress; // 0.0 to 1.0
    private String text;

    public CircularProgressPanel() {
        this.progress = 0.0;
        this.text = "0%";
        setOpaque(false);
        setPreferredSize(new Dimension(200, 200));
    }

    public void updateProgress(double progress) {
        this.progress = Math.max(0.0, Math.min(1.0, progress));
        this.text = String.format("%d%%", (int) (this.progress * 100));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 20;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;
        int thickness = 20;

        // Background circle
        g2d.setColor(Style.BACKGROUND_COLOR);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawOval(x, y, size, size);

        // Progress arc
        g2d.setColor(Style.PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(new Arc2D.Double(x, y, size, size, 90, -progress * 360, Arc2D.OPEN));

        // Text in the center
        g2d.setColor(Style.TEXT_PRIMARY_COLOR);
        g2d.setFont(Style.FONT_HEADER.deriveFont(48f));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g2d.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 4);
        
        g2d.setFont(Style.FONT_LABEL);
        fm = g2d.getFontMetrics();
        g2d.drawString("Completed", getWidth() / 2 - fm.stringWidth("Completed") / 2, getHeight() / 2 + textHeight / 4 + 30);


        g2d.dispose();
    }
}

class BarChartPanel extends JPanel {
    private Map<String, Integer> data; // Label -> Value
    private Color[] barColors = {Style.CHART_COLOR_1, Style.CHART_COLOR_2, Style.CHART_COLOR_3, Style.CHART_COLOR_4};

    public BarChartPanel() {
        this.data = new LinkedHashMap<>();
        setOpaque(false);
        setPreferredSize(new Dimension(400, 250));
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
        this.barColors = new Color[]{Style.CHART_COLOR_1, Style.CHART_COLOR_2, Style.CHART_COLOR_3, Style.CHART_COLOR_4}; // CHANGED: Refresh colors on data change
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int maxVal = data.values().stream().max(Integer::compareTo).orElse(1);
        int width = getWidth();
        int height = getHeight();
        int padding = 25;
        int labelPadding = 20;

        // Draw Y-axis labels and grid lines
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        for (int i = 0; i <= 4; i++) {
            int y = height - padding - labelPadding - i * (height - padding * 2 - labelPadding) / 4;
            int value = (int)Math.ceil(((double)i / 4.0) * maxVal);
            g2d.drawLine(padding, y, width - padding, y);
            g2d.drawString(String.valueOf(value), padding - fm.stringWidth(String.valueOf(value)) - 5, y + 5);
        }

        // Draw bars
        int barWidth = (width - 2 * padding) / (data.size()*2);
        int x = padding + barWidth/2;
        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int barHeight = (int) (((double) entry.getValue() / maxVal) * (height - 2 * padding - labelPadding));
            int y = height - padding - labelPadding - barHeight;
            
            g2d.setColor(barColors[colorIndex % barColors.length]);
            g2d.fillRoundRect(x, y, barWidth, barHeight, 10, 10);
            
            // Draw X-axis label
            g2d.setColor(Style.TEXT_SECONDARY_COLOR);
            g2d.drawString(entry.getKey(), x + barWidth/2 - fm.stringWidth(entry.getKey())/2, height - padding);
            
            x += 2*barWidth;
            colorIndex++;
        }
        g2d.dispose();
    }
}


// --- USER DASHBOARD (Completely redesigned) ---
class UserDashboard extends JFrame {
    private final User user; private final LoginPage loginPageInstance; private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private DashboardPanel dashboardPanel;

    public UserDashboard(LoginPage lp, User u) {
        this.user = u; this.loginPageInstance = lp; setTitle("Todo Petal | " + u.getUsername()); setExtendedState(JFrame.MAXIMIZED_BOTH); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel basePanel = new JPanel(new BorderLayout());
        basePanel.setBackground(Style.BACKGROUND_COLOR);
        
        basePanel.add(createSidebar(), BorderLayout.WEST);
        
        dashboardPanel = new DashboardPanel(this, user);
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.setOpaque(false);
        
        basePanel.add(mainPanel, BorderLayout.CENTER);
        
        add(basePanel);
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(); sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Style.CARD_BACKGROUND_COLOR); sidebar.setPreferredSize(new Dimension(250, 0)); sidebar.setBorder(new EmptyBorder(20,10,20,10));
        JLabel appName = new JLabel("🌸 Todo Petal"); appName.setFont(Style.FONT_FORM_HEADER); appName.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(appName); sidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        sidebar.add(createSidebarItem("📊 Dashboard", true)); sidebar.add(createSidebarItem("✔️ Tasks", false)).addMouseListener(new MouseAdapter() {
             public void mouseClicked(MouseEvent e) { showTaskListView(); }
        });
        sidebar.add(Box.createVerticalGlue());
        
        sidebar.add(createSidebarItem("⚙️ Settings", false)).addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                Object[] options = { "Aqua Dream", "Lavender Bliss", "Forest Green" }; // ADDED: New theme option
                int choice = JOptionPane.showOptionDialog(UserDashboard.this, "Select a theme:", "Settings", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (choice == 0) Style.applyTheme(Theme.AQUA_DREAM); // ADDED: Apply new theme
                else if (choice == 1) Style.applyTheme(Theme.LAVENDER_BLISS);
                else if (choice == 2) Style.applyTheme(Theme.FOREST_GREEN);
                SwingUtilities.updateComponentTreeUI(UserDashboard.this);
            }
        });
        sidebar.add(createSidebarItem("↩️ Logout", false)).addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { dispose(); loginPageInstance.setVisible(true); }
        });
        return sidebar;
    }

    private JLabel createSidebarItem(String text, boolean isActive) {
        JLabel label = new JLabel(text); label.setFont(Style.FONT_SIDEBAR); label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setBorder(new EmptyBorder(15, 20, 15, 20)); label.setAlignmentX(Component.CENTER_ALIGNMENT);
        if(isActive) { label.setForeground(Style.PRIMARY_COLOR); } else { label.setForeground(Style.TEXT_SECONDARY_COLOR); }
        return label;
    }

    public void showTaskEditor(Task t) { 
        JPanel ev = new TaskEditorPanel(user, t, () -> { 
            dashboardPanel.refreshData(); // Refresh dashboard stats
            cardLayout.show(mainPanel, "DASHBOARD"); 
        }); 
        mainPanel.add(ev, "EDITOR"); cardLayout.show(mainPanel, "EDITOR"); 
    }
    
    public void showTaskListView() {
        TaskListView taskListPanel = new TaskListView(this, user);
        mainPanel.add(taskListPanel, "TASK_LIST");
        cardLayout.show(mainPanel, "TASK_LIST");
    }

    public void showDashboard() {
        dashboardPanel.refreshData();
        cardLayout.show(mainPanel, "DASHBOARD");
    }
}

class DashboardPanel extends JPanel {
    private final User user;
    private final UserDashboard parentFrame;
    private final TaskDAO taskDAO = new TaskDAO();
    private JLabel todoCount, inProgressCount, doneCount, welcomeLabel;
    private CircularProgressPanel progressCircle;
    private BarChartPanel barChart;

    public DashboardPanel(UserDashboard parent, User user) {
        this.parentFrame = parent;
        this.user = user;
        setBackground(Style.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; gbc.weighty = 0.1;
        add(createHeader(), gbc);

        // Stats cards
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weighty = 0.2;
        gbc.gridx = 0; add(createStatCard("To Do", todoCount = new JLabel("0"), Style.STATUS_TODO), gbc);
        gbc.gridx = 1; add(createStatCard("In Progress", inProgressCount = new JLabel("0"), Style.STATUS_IN_PROGRESS), gbc);
        gbc.gridx = 2; add(createStatCard("Done", doneCount = new JLabel("0"), Style.STATUS_DONE), gbc);

        // Bar Chart
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 0.7;
        add(createChartCard(), gbc);

        // Progress Circle
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 1;
        add(createProgressCard(), gbc);
        
        refreshData();
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        welcomeLabel = new JLabel("Welcome back, " + user.getUsername() + "!");
        welcomeLabel.setFont(Style.FONT_HEADER);
        
        RoundedButton addTaskBtn = new RoundedButton("+ Add New Task");
        addTaskBtn.setBackground(Style.PRIMARY_COLOR);
        addTaskBtn.setForeground(Color.WHITE);
        addTaskBtn.setPreferredSize(new Dimension(200, 50));
        addTaskBtn.addActionListener(e -> parentFrame.showTaskEditor(null));
        
        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(addTaskBtn, BorderLayout.EAST);
        return header;
    }

    private RoundedPanel createStatCard(String title, JLabel valueLabel, Color color) {
        RoundedPanel card = new RoundedPanel(25);
        card.setBackground(Style.CARD_BACKGROUND_COLOR);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel indicator = new JPanel();
        indicator.setBackground(color);
        indicator.setPreferredSize(new Dimension(8, 0));
        card.add(indicator, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Style.FONT_CARD_TITLE);
        titleLabel.setForeground(Style.TEXT_SECONDARY_COLOR);
        valueLabel.setFont(Style.FONT_CARD_VALUE);
        content.add(titleLabel);
        content.add(valueLabel);
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private RoundedPanel createChartCard() {
        RoundedPanel card = new RoundedPanel(25);
        card.setBackground(Style.CARD_BACKGROUND_COLOR);
        card.setLayout(new BorderLayout(10,10));
        card.setBorder(new EmptyBorder(15,15,15,15));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Activity Overview");
        title.setFont(Style.FONT_CARD_TITLE);
        
        // Buttons for chart time frame
        JToggleButton daily = new JToggleButton("Daily");
        JToggleButton weekly = new JToggleButton("Weekly");
        JToggleButton monthly = new JToggleButton("Monthly");
        ButtonGroup group = new ButtonGroup();
        group.add(daily); group.add(weekly); group.add(monthly);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(daily); buttonPanel.add(weekly); buttonPanel.add(monthly);
        
        header.add(title, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);
        
        barChart = new BarChartPanel();
        card.add(barChart, BorderLayout.CENTER);

        ActionListener listener = e -> updateChart(e.getActionCommand());
        daily.addActionListener(listener);
        weekly.addActionListener(listener);
        monthly.addActionListener(listener);
        weekly.setSelected(true); // Default selection
        updateChart("Weekly");

        return card;
    }
    
    private RoundedPanel createProgressCard() {
        RoundedPanel card = new RoundedPanel(25);
        card.setBackground(Style.CARD_BACKGROUND_COLOR);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15,15,15,15));
        
        JLabel title = new JLabel("Overall Progress", SwingConstants.CENTER);
        title.setFont(Style.FONT_CARD_TITLE);
        card.add(title, BorderLayout.NORTH);

        progressCircle = new CircularProgressPanel();
        card.add(progressCircle, BorderLayout.CENTER);
        
        return card;
    }

    public void refreshData() {
        Map<String, Long> counts = taskDAO.getTaskCountByStatus(user.getId());
        long todo = counts.getOrDefault("TODO", 0L);
        long inProg = counts.getOrDefault("IN_PROGRESS", 0L);
        long done = counts.getOrDefault("DONE", 0L);
        long total = todo + inProg + done;

        todoCount.setText(String.valueOf(todo));
        inProgressCount.setText(String.valueOf(inProg));
        doneCount.setText(String.valueOf(done));

        double progress = (total == 0) ? 0.0 : (double) done / total;
        progressCircle.updateProgress(progress);
    }
    
    private void updateChart(String period) {
        LocalDate end = LocalDate.now();
        LocalDate start;
        switch (period) {
            case "Daily":
                start = end.minusDays(6);
                break;
            case "Monthly":
                start = end.with(TemporalAdjusters.firstDayOfMonth());
                break;
            case "Weekly":
            default:
                start = end.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                break;
        }
        barChart.setData(taskDAO.getCompletedTasksByDay(user.getId(), start, end));
    }
}

// --- TASK LIST VIEW (The old dashboard view) ---
class TaskListView extends JPanel {
    private final User user; private DefaultListModel<Task> listModel; private JList<Task> taskList;
    private JComboBox<String> sortBox, filterBox; private int hoveredIndex = -1;
    private final TaskDAO taskDAO = new TaskDAO();
    private final UserDashboard parentFrame;

    public TaskListView(UserDashboard parent, User user) {
        this.parentFrame = parent;
        this.user = user;
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(15, 25, 15, 25));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel todayLabel = new JLabel("My Tasks");
        todayLabel.setFont(Style.FONT_HEADER);

        // Header controls
        JPanel headerControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerControls.setOpaque(false);
        RoundedButton addBtn = new RoundedButton("+ Add New");
        addBtn.setBackground(Style.PRIMARY_COLOR);
        addBtn.setForeground(Color.WHITE);
        addBtn.setPreferredSize(new Dimension(150, 50));
        headerControls.add(addBtn);

        headerPanel.add(todayLabel, BorderLayout.WEST);
        headerPanel.add(headerControls, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with controls and list
        JPanel centerPanel = new JPanel(new BorderLayout(10, 20));
        centerPanel.setOpaque(false);
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setOpaque(false);

        JPanel sortFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortFilterPanel.setOpaque(false);
        sortFilterPanel.add(new JLabel("Sort by:"));
        sortBox = new JComboBox<>(new String[]{"Status", "Due Date", "Priority"});
        sortFilterPanel.add(sortBox);
        sortFilterPanel.add(new JLabel("  Filter:"));
        filterBox = new JComboBox<>(new String[]{"All Tasks", "Active Tasks", "Done Tasks"});
        sortFilterPanel.add(filterBox);
        
        controlsPanel.add(sortFilterPanel, BorderLayout.EAST);
        centerPanel.add(controlsPanel, BorderLayout.NORTH);
        
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCardRenderer(() -> hoveredIndex));
        taskList.setBackground(Style.BACKGROUND_COLOR);
        taskList.setSelectionModel(new DefaultListSelectionModel() { @Override public void setSelectionInterval(int i, int i1) {} });
        
        loadUserTasks();
        JScrollPane sp = new JScrollPane(taskList);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        centerPanel.add(sp, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        addBtn.addActionListener(e -> parentFrame.showTaskEditor(null));
        sortBox.addActionListener(e -> loadUserTasks());
        filterBox.addActionListener(e -> loadUserTasks());
        
        taskList.addMouseMotionListener(new MouseAdapter() { 
            @Override public void mouseMoved(MouseEvent e) { 
                int i = taskList.locationToIndex(e.getPoint()); 
                if (i != hoveredIndex) { hoveredIndex = i; taskList.repaint(); } 
            } 
        });
        taskList.addMouseListener(new MouseAdapter() { 
            @Override public void mouseExited(MouseEvent e) { hoveredIndex = -1; taskList.repaint(); } 
            @Override public void mouseClicked(MouseEvent e) { 
                if (e.getClickCount() == 2) { 
                    int i = taskList.locationToIndex(e.getPoint()); 
                    if (i >= 0) parentFrame.showTaskEditor(listModel.getElementAt(i)); 
                } 
            } 
        });
    }

    private void loadUserTasks() {
        listModel.clear(); 
        List<Task> ts = taskDAO.getTasksForUser(user.getId()); 
        if (ts != null) { 
            String f = (String) filterBox.getSelectedItem();
            List<Task> filtered = ts.stream().filter(t -> "All Tasks".equals(f) || ("Active Tasks".equals(f) && t.getStatus() != Status.DONE) || ("Done Tasks".equals(f) && t.getStatus() == Status.DONE)).collect(Collectors.toList());
            String s = (String) sortBox.getSelectedItem(); 
            if ("Due Date".equals(s)) filtered.sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
            else if ("Priority".equals(s)) filtered.sort(Comparator.comparing(Task::getPriority)); 
            else if ("Status".equals(s)) filtered.sort(Comparator.comparing(Task::getStatus));
            filtered.forEach(listModel::addElement);
        }
    }
}


// --- DEDICATED TASK EDITOR PANEL (Updated for DAO) ---
class TaskEditorPanel extends JPanel {
    private final User user; private Task task; private final Runnable onSaveCallback;
    private JTextField titleField; private JTextArea descArea; private JComboBox<Priority> priorityBox; private JComboBox<Category> categoryBox;
    private JComboBox<Status> statusBox; private JLabel selectedDateLabel; private LocalDate selectedDate;
    private final boolean isNewTask;
    private final TaskDAO taskDAO = new TaskDAO();

    public TaskEditorPanel(User user, Task task, Runnable onSaveCallback) {
        this.user = user; 
        this.isNewTask = (task == null);
        this.task = isNewTask ? new Task(user.getId(), "", Priority.MEDIUM, Category.OTHER, LocalDate.now(), Status.TODO) : task;
        this.onSaveCallback = onSaveCallback; this.selectedDate = this.task.getDueDate();
        
        setBackground(Style.BACKGROUND_COLOR); setBorder(new EmptyBorder(20, 100, 20, 100)); setLayout(new BorderLayout(20, 20));
        String titleText = isNewTask ? "Create New Task" : "Edit Task";
        JLabel title = new JLabel(titleText, SwingConstants.CENTER); title.setFont(Style.FONT_HEADER); add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout()); formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(10, 5, 10, 5); gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; formPanel.add(new JLabel("Task Title"), gbc);
        gbc.gridy++; titleField = new JTextField(this.task.getDescription().split("\n")[0]); titleField.setFont(Style.FONT_FIELD); formPanel.add(titleField, gbc);
        gbc.gridy++; formPanel.add(new JLabel("Detailed Description (add subtasks like [ ] or [x])"), gbc);
        gbc.gridy++; descArea = new JTextArea(5, 20); descArea.setFont(Style.FONT_FIELD); descArea.setLineWrap(true); descArea.setWrapStyleWord(true);
        String[] descParts = this.task.getDescription().split("\n", 2); if (descParts.length > 1) { descArea.setText(descParts[1]); }
        formPanel.add(new JScrollPane(descArea), gbc);
        
        gbc.gridy++; gbc.gridwidth = 1; formPanel.add(new JLabel("Priority"), gbc);
        gbc.gridy++; priorityBox = new JComboBox<>(Priority.values()); priorityBox.setSelectedItem(this.task.getPriority()); formPanel.add(priorityBox, gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(new JLabel("Category"), gbc);
        gbc.gridy++; categoryBox = new JComboBox<>(Category.values()); categoryBox.setSelectedItem(this.task.getCategory()); formPanel.add(categoryBox, gbc);
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Status"), gbc);
        gbc.gridy++; statusBox = new JComboBox<>(Status.values()); statusBox.setSelectedItem(this.task.getStatus()); formPanel.add(statusBox, gbc);
        
        gbc.gridx = 1; gbc.gridy = 6; JPanel datePanel = new JPanel(new BorderLayout()); datePanel.setOpaque(false);
        RoundedButton calBtn = new RoundedButton("Select Date"); calBtn.setBackground(Style.ACCENT_COLOR); calBtn.setForeground(Color.WHITE);
        selectedDateLabel = new JLabel(); updateSelectedDateLabel(); datePanel.add(calBtn, BorderLayout.NORTH); datePanel.add(selectedDateLabel, BorderLayout.SOUTH);
        gbc.gridy++; formPanel.add(datePanel, gbc); add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); buttonPanel.setOpaque(false);
        RoundedButton saveBtn = new RoundedButton(isNewTask ? "Create Task" : "Save Changes"); saveBtn.setBackground(Style.PRIMARY_COLOR);
        saveBtn.setForeground(Color.WHITE); saveBtn.setPreferredSize(new Dimension(200, 50));
        RoundedButton cancelBtn = new RoundedButton("Cancel"); cancelBtn.setBackground(Style.TEXT_SECONDARY_COLOR);
        cancelBtn.setForeground(Color.WHITE); cancelBtn.setPreferredSize(new Dimension(150, 50));
        buttonPanel.add(saveBtn); buttonPanel.add(cancelBtn); add(buttonPanel, BorderLayout.SOUTH);
        
        calBtn.addActionListener(e -> openCalendar()); saveBtn.addActionListener(e -> saveTask()); cancelBtn.addActionListener(e -> onSaveCallback.run());
    }

    private void openCalendar() { JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Date", true); JCalendarPanel cp = new JCalendarPanel(selectedDate); d.add(cp); d.pack(); d.setLocationRelativeTo(this); d.setVisible(true); if(cp.getSelectedDate() != null) { this.selectedDate = cp.getSelectedDate(); updateSelectedDateLabel(); } }
    private void updateSelectedDateLabel() { selectedDateLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))); }
    private void saveTask() { 
        if (titleField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        
        String fullDesc = titleField.getText().trim() + "\n" + descArea.getText().trim();
        task.setDescription(fullDesc); task.setPriority((Priority) priorityBox.getSelectedItem());
        task.setCategory((Category) categoryBox.getSelectedItem()); task.setStatus((Status) statusBox.getSelectedItem());
        task.setDueDate(selectedDate); 
        
        if (isNewTask) taskDAO.addTask(task); else taskDAO.updateTask(task);
        onSaveCallback.run();
    }
}

// --- INTERACTIVE CALENDAR COMPONENT ---
class JCalendarPanel extends JPanel {
    private LocalDate currentDate; private LocalDate selectedDate; private JLabel monthLabel; private JPanel daysPanel;
    public JCalendarPanel(LocalDate initialDate) {
        this.selectedDate = initialDate; this.currentDate = initialDate.withDayOfMonth(1); setLayout(new BorderLayout(10, 10)); setBorder(new EmptyBorder(10,10,10,10));
        JPanel h = new JPanel(new BorderLayout()); JButton p = new JButton("<"); JButton n = new JButton(">"); monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(Style.FONT_BUTTON); h.add(p, BorderLayout.WEST); h.add(monthLabel, BorderLayout.CENTER); h.add(n, BorderLayout.EAST); add(h, BorderLayout.NORTH);
        daysPanel = new JPanel(new GridLayout(0, 7, 5, 5)); add(daysPanel, BorderLayout.CENTER);
        p.addActionListener(e -> { currentDate = currentDate.minusMonths(1); drawCalendar(); }); n.addActionListener(e -> { currentDate = currentDate.plusMonths(1); drawCalendar(); });
        drawCalendar();
    }
    private void drawCalendar() {
        daysPanel.removeAll(); monthLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; for(String name : dayNames) { daysPanel.add(new JLabel(name, SwingConstants.CENTER)); }
        int firstDayOfWeek = currentDate.getDayOfWeek().getValue() % 7; for(int i = 0; i < firstDayOfWeek; i++) { daysPanel.add(new JLabel("")); }
        for(int day = 1; day <= currentDate.lengthOfMonth(); day++) { final int d = day; JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            dayLabel.setOpaque(true); dayLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); LocalDate thisDate = currentDate.withDayOfMonth(day);
            if(thisDate.equals(selectedDate)) { dayLabel.setBackground(Style.PRIMARY_COLOR); dayLabel.setForeground(Color.WHITE); }
            else if (thisDate.equals(LocalDate.now())) { dayLabel.setBackground(Style.ACCENT_COLOR); dayLabel.setForeground(Color.WHITE); }
            else { dayLabel.setBackground(Style.CARD_BACKGROUND_COLOR); }
            dayLabel.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { selectedDate = currentDate.withDayOfMonth(d); Window w = SwingUtilities.getWindowAncestor(JCalendarPanel.this); if (w instanceof JDialog) ((JDialog) w).dispose(); } });
            daysPanel.add(dayLabel);
        } revalidate(); repaint();
    }
    public LocalDate getSelectedDate() { return selectedDate; }
}

// --- CUSTOM RENDERERS ---
class TaskCardRenderer implements ListCellRenderer<Task> {
    private final java.util.function.Supplier<Integer> hoveredIndexSupplier;
    public TaskCardRenderer(java.util.function.Supplier<Integer> h) { this.hoveredIndexSupplier = h; }
    @Override public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
        RoundedPanel card = new RoundedPanel(25); card.setBackground(Style.CARD_BACKGROUND_COLOR); card.setLayout(new BorderLayout(15, 15)); card.setBorder(new EmptyBorder(20, 20, 20, 20));
        if (index == hoveredIndexSupplier.get()) card.setBorderColor(Style.PRIMARY_COLOR);
        
        JPanel westPanel = new JPanel(); westPanel.setOpaque(false); westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS)); westPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        JPanel pi = new JPanel(); pi.setPreferredSize(new Dimension(8, 8)); pi.setBackground(task.getPriority() == Priority.HIGH ? Style.PRIORITY_HIGH : task.getPriority() == Priority.MEDIUM ? Style.PRIORITY_MEDIUM : Style.PRIORITY_LOW);
        westPanel.add(pi); card.add(westPanel, BorderLayout.WEST);

        JPanel details = new JPanel(); details.setOpaque(false); details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        String title = task.getDescription().split("\n")[0]; JLabel dl = new JLabel(title); dl.setFont(Style.FONT_TASK_DESC);
        String dt = task.getCategory().icon + " " + task.getCategory().name() + "  |  Due: " + task.getDueDate().format(DateTimeFormatter.ofPattern("dd MMM"));
        JLabel sl = new JLabel(dt); sl.setFont(Style.FONT_TASK_DETAILS); sl.setForeground(Style.TEXT_SECONDARY_COLOR);
        if (task.getStatus() == Status.DONE) { dl.setText("<html><strike>" + title + "</strike></html>"); dl.setForeground(Style.TEXT_SECONDARY_COLOR); }
        else { dl.setForeground(Style.TEXT_PRIMARY_COLOR); }
        details.add(dl); details.add(Box.createRigidArea(new Dimension(0, 5))); details.add(sl);
        
        String[] lines = task.getDescription().split("\n"); int totalSubtasks = 0; int completedSubtasks = 0;
        for (String line : lines) { if (line.trim().startsWith("[ ]")) totalSubtasks++; if (line.trim().startsWith("[x]")) { totalSubtasks++; completedSubtasks++; } }
        if (totalSubtasks > 0) {
            details.add(Box.createRigidArea(new Dimension(0, 8)));
            JLabel subtaskLabel = new JLabel("Subtasks: " + completedSubtasks + "/" + totalSubtasks + " completed");
            subtaskLabel.setFont(Style.FONT_TASK_DETAILS); subtaskLabel.setForeground(Style.TEXT_SECONDARY_COLOR);
            details.add(subtaskLabel);
        }
        card.add(details, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel(task.getStatus().toString().replace("_", " ")); statusLabel.setFont(Style.FONT_TASK_DETAILS); statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true); statusLabel.setBorder(new EmptyBorder(3,8,3,8));
        switch (task.getStatus()) { case TODO: statusLabel.setBackground(Style.STATUS_TODO); break; case IN_PROGRESS: statusLabel.setBackground(Style.STATUS_IN_PROGRESS); break; case DONE: statusLabel.setBackground(Style.STATUS_DONE); break; }
        card.add(statusLabel, BorderLayout.EAST); return card;
    }
}
class ModernTableCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable t, Object v, boolean is, boolean hf, int r, int c) {
        Component co = super.getTableCellRendererComponent(t, v, is, hf, r, c);
        co.setBackground(is ? t.getSelectionBackground() : r % 2 == 0 ? Style.CARD_BACKGROUND_COLOR : Style.BACKGROUND_COLOR); // CHANGED
        setBorder(new EmptyBorder(5, 10, 5, 10)); return co;
    }
}

// --- ADMIN DASHBOARD (Updated for DAO) ---
class AdminDashboard extends JFrame {
    private final LoginPage loginPageInstance; private DefaultTableModel userTableModel; private JTable userTable;
    private final UserDAO userDAO = new UserDAO();
    private JLabel totalUsersLabel;
    
    public AdminDashboard(LoginPage lp, User adminUser) {
        this.loginPageInstance = lp; setTitle("Todo Petal - Admin Panel"); setExtendedState(JFrame.MAXIMIZED_BOTH); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15)); getContentPane().setBackground(Style.BACKGROUND_COLOR); getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel hp = new JPanel(new BorderLayout()); hp.setOpaque(false); JLabel h = new JLabel("Admin Dashboard"); h.setFont(Style.FONT_HEADER);
        hp.add(h, BorderLayout.WEST); RoundedButton lob = new RoundedButton("Logout"); lob.setBackground(Style.DANGER_COLOR);
        lob.setForeground(Color.WHITE); lob.setPreferredSize(new Dimension(140, 45)); lob.addActionListener(e -> { dispose(); loginPageInstance.setVisible(true); });
        hp.add(lob, BorderLayout.EAST); add(hp, BorderLayout.NORTH); 
        
        JPanel mc = new JPanel(new BorderLayout(15, 15)); mc.setOpaque(false);
        RoundedPanel sp = createStatsPanel(); mc.add(sp, BorderLayout.NORTH); 
        
        RoundedPanel up = new RoundedPanel(25); up.setBackground(Style.CARD_BACKGROUND_COLOR);
        up.setLayout(new BorderLayout(10, 10)); up.setBorder(new EmptyBorder(10, 10, 10, 10));
        userTableModel = new DefaultTableModel(new String[]{"ID", "Username", "Role", "Email", "Phone", "Last Active"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        userTable = new JTable(userTableModel); setupModernTable(userTable); 
        
        up.add(new JScrollPane(userTable), BorderLayout.CENTER);
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bp.setOpaque(false); bp.setBackground(Style.CARD_BACKGROUND_COLOR);
        RoundedButton addBtn = new RoundedButton("Add User"); addBtn.setBackground(Style.PRIMARY_COLOR); addBtn.setForeground(Color.WHITE); bp.add(addBtn);
        RoundedButton deleteBtn = new RoundedButton("Delete User"); deleteBtn.setBackground(Style.DANGER_COLOR); deleteBtn.setForeground(Color.WHITE); bp.add(deleteBtn);
        up.add(bp, BorderLayout.SOUTH); 
        
        addBtn.addActionListener(e -> addNewUser()); deleteBtn.addActionListener(e -> deleteSelectedUser());
        mc.add(up, BorderLayout.CENTER); add(mc, BorderLayout.CENTER); 
        loadUsers();
    }
    private RoundedPanel createStatsPanel() { 
        RoundedPanel p = new RoundedPanel(25); p.setBackground(Style.CARD_BACKGROUND_COLOR); p.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 20)); 
        totalUsersLabel = new JLabel("Total Users: 0"); totalUsersLabel.setFont(Style.FONT_FORM_HEADER); totalUsersLabel.setForeground(Style.TEXT_PRIMARY_COLOR); p.add(totalUsersLabel); return p; 
    }
    
    private void loadUsers() { 
        userTableModel.setRowCount(0); 
        List<User> users = userDAO.getAllUsers();
        for (User u : users) { userTableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), u.getEmail(), u.getPhone(), formatTimeAgo(u.getLastLoginTime())}); }
        totalUsersLabel.setText("Total Users: " + users.size());
    }

    private String formatTimeAgo(LocalDateTime t) { if (t == null) return "Never"; Duration d = Duration.between(t, LocalDateTime.now()); long s = d.getSeconds(); if (s < 60) return "Online Now"; if (s < 3600) return (s / 60) + "m ago"; if (s < 86400) return (s / 3600) + "h ago"; if (s < 172800) return "Yesterday"; return t.format(DateTimeFormatter.ofPattern("dd MMM yyyy")); }
    private void addNewUser() { 
        UserEditorDialog dialog = new UserEditorDialog(this, null); dialog.setVisible(true); 
        if(dialog.isSaved()) { 
            User newUser = dialog.getUser(); 
            if (userDAO.addUser(newUser)) loadUsers();
            else JOptionPane.showMessageDialog(this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
    
    private void deleteSelectedUser() { 
        int row = userTable.getSelectedRow(); if(row == -1) { JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Error", JOptionPane.WARNING_MESSAGE); return; } 
        int userId = (int) userTableModel.getValueAt(row, 0);
        String username = (String) userTableModel.getValueAt(row, 1);
        if(username.equals("admin")) { JOptionPane.showMessageDialog(this, "Cannot delete the primary admin account.", "Error", JOptionPane.ERROR_MESSAGE); return; } 
        int c = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user '" + username + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION); 
        if(c == JOptionPane.YES_OPTION) { 
            if (userDAO.deleteUser(userId)) loadUsers();
            else JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setupModernTable(JTable t) { 
        t.setRowHeight(40); t.setGridColor(Style.BACKGROUND_COLOR); t.setSelectionBackground(Style.PRIMARY_COLOR); t.setSelectionForeground(Color.WHITE); t.setDefaultRenderer(Object.class, new ModernTableCellRenderer()); JTableHeader h = t.getTableHeader(); h.setBackground(Style.BACKGROUND_COLOR); h.setForeground(Style.TEXT_PRIMARY_COLOR); h.setPreferredSize(new Dimension(100, 40));
        // Hide ID column
        t.getColumnModel().getColumn(0).setMinWidth(0);
        t.getColumnModel().getColumn(0).setMaxWidth(0);
        t.getColumnModel().getColumn(0).setWidth(0);
    }
}

// --- DIALOG FOR ADMIN TO ADD/EDIT USERS (Updated for DAO)---
class UserEditorDialog extends JDialog {
    private boolean saved = false; private User user;
    private JTextField userField, emailField, phoneField; private JPasswordField passField; private JComboBox<String> roleBox;
    private UserDAO userDAO = new UserDAO();
    
    public UserEditorDialog(Frame owner, User userToEdit) {
        super(owner, (userToEdit == null ? "Add New User" : "Edit User"), true);
        this.user = (userToEdit == null) ? new User("", "", "User", "", "") : userToEdit;
        setSize(450, 400); setLocationRelativeTo(owner); setLayout(new BorderLayout(10, 10)); getRootPane().setBorder(new EmptyBorder(10,10,10,10));
        
        JPanel form = new JPanel(new GridBagLayout()); GridBagConstraints gbc = new GridBagConstraints(); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 5, 5); gbc.weightx = 1.0;
        userField = new JTextField(user.getUsername()); if(userToEdit != null) userField.setEnabled(false);
        passField = new JPasswordField(); emailField = new JTextField(user.getEmail()); phoneField = new JTextField(user.getPhone()); roleBox = new JComboBox<>(new String[]{"User", "Admin"});
        
        gbc.gridy = 0; form.add(new JLabel("Username:"), gbc); gbc.gridy++; form.add(userField, gbc);
        gbc.gridy++; form.add(new JLabel("Password:"), gbc); gbc.gridy++; form.add(passField, gbc);
        gbc.gridy++; form.add(new JLabel("Email:"), gbc); gbc.gridy++; form.add(emailField, gbc);
        gbc.gridy++; form.add(new JLabel("Phone:"), gbc); gbc.gridy++; form.add(phoneField, gbc);
        gbc.gridy++; form.add(new JLabel("Role:"), gbc); gbc.gridy++; form.add(roleBox, gbc);
        add(form, BorderLayout.CENTER);
        
        RoundedButton saveBtn = new RoundedButton("Save"); saveBtn.setBackground(Style.PRIMARY_COLOR); saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveUser()); add(saveBtn, BorderLayout.SOUTH);
    }
    private void saveUser() {
        String u = userField.getText().trim(); String p = new String(passField.getPassword());
        if(u.isEmpty() || p.isEmpty()) { JOptionPane.showMessageDialog(this, "Username and Password are required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        if(userDAO.getUserByUsername(u).isPresent() && user.getId() == 0) { JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        
        this.user = new User(u, p, (String)roleBox.getSelectedItem(), emailField.getText().trim(), phoneField.getText().trim());
        saved = true; dispose();
    }
    public boolean isSaved() { return saved; }
    public User getUser() { return user; }
}