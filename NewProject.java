// These are libraries.
import java.awt.*;      // Used to create the window like buttons,labels, textfield, and frames
import java.awt.event.*;    //  Used to detect button clicks.
import java.sql.*;      // Used to connect Java with MySQL Database.

public class NewProject extends Frame {     // We are creating a class called NewProject and it is a frame = window

    //These are variables.
    private TextField idField;
    private TextField passwordField;
    private Button loginButton;

    // ── Shared colours / fonts used across the app (keeps the look consistent) ─
    private static final Color COLOR_HEADER      = new Color(25, 60, 120);
    private static final Color COLOR_BG          = new Color(245, 247, 250);
    private static final Color COLOR_CARD        = Color.WHITE;
    private static final Color COLOR_PRIMARY     = new Color(30, 130, 76);   // green - primary actions
    private static final Color COLOR_SECONDARY   = new Color(70, 130, 180);  // blue - view/secondary actions
    private static final Color COLOR_DANGER      = new Color(180, 60, 40);   // red - clear/logout
    private static final Color COLOR_NEUTRAL     = new Color(120, 120, 120); // grey - back
    private static final Color COLOR_NAVBAR      = new Color(230, 233, 238);
    private static final Font  FONT_HEADER       = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font  FONT_LABEL        = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font  FONT_BUTTON       = new Font("Segoe UI", Font.BOLD, 13);

    // Database information - This tells Java My database is located here.
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/coworkspacebooking";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // ── Constructor (Login Page) ────────────────────────────────────────────────
    public NewProject() {
        super("Staff Login - Co-Working Space Booking System");

        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // Header banner
        Panel headerPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 22));
        headerPanel.setBackground(COLOR_HEADER);
        Label headerLabel = new Label("Co-Working Space Booking System");
        headerLabel.setFont(FONT_HEADER);
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Centered login card
        Panel outerWrap = new Panel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        outerWrap.setBackground(COLOR_BG);

        Panel loginCard = new Panel(new BorderLayout(10, 10));
        loginCard.setBackground(COLOR_CARD);
        loginCard.setPreferredSize(new Dimension(380, 210));

        Label cardTitle = new Label("Staff Sign In", Label.CENTER);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cardTitle.setForeground(COLOR_HEADER);

        Panel fieldsPanel = new Panel(new GridLayout(2, 2, 12, 18));
        fieldsPanel.setBackground(COLOR_CARD);

        Label idLabel = new Label("Staff ID:");
        idLabel.setFont(FONT_LABEL);
        idField = new TextField();
        idField.setFont(FONT_LABEL);

        Label passLabel = new Label("Password:");
        passLabel.setFont(FONT_LABEL);
        passwordField = new TextField();
        passwordField.setEchoChar('*');         // For Password Hiding
        passwordField.setFont(FONT_LABEL);

        fieldsPanel.add(idLabel);
        fieldsPanel.add(idField);
        fieldsPanel.add(passLabel);
        fieldsPanel.add(passwordField);

        Panel buttonWrap = new Panel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonWrap.setBackground(COLOR_CARD);
        loginButton = createStyledButton("Login", COLOR_PRIMARY);
        loginButton.setPreferredSize(new Dimension(140, 32));
        buttonWrap.add(loginButton);

        Panel cardPadded = new Panel(new BorderLayout(0, 15));
        cardPadded.setBackground(COLOR_CARD);
        cardPadded.add(cardTitle, BorderLayout.NORTH);
        cardPadded.add(fieldsPanel, BorderLayout.CENTER);
        cardPadded.add(buttonWrap, BorderLayout.SOUTH);

        loginCard.add(cardPadded, BorderLayout.CENTER);
        outerWrap.add(loginCard);

        add(headerPanel, BorderLayout.NORTH);
        add(outerWrap, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {    // When the Login button is clicked then run this code
            public void actionPerformed(ActionEvent e) {

                String id   = idField.getText().trim();             // Suppose user types 101 Then id = "101"
                String pass = passwordField.getText().trim();
                checkLogin(id, pass);       // This sends the entered values to another method       kinda verification
            }
        });

        addWindowListener(new WindowAdapter() {     // When user clicks X means close
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setSize(720, 460);
        setLocationRelativeTo(null);   // Centers the window on the screen
        setResizable(false);
        setVisible(true);   // Without this,the window would never appear This command tells Java Show the window.
    }

    // ── Styled button / navigation helpers ──────────────────────────────────────
    private Button createStyledButton(String label, Color bg) {
        Button b = new Button(label);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BUTTON);
        return b;
    }

    private Button createLogoutButton(Frame currentFrame) {
        Button logoutButton = createStyledButton("Logout", COLOR_DANGER);
        logoutButton.addActionListener(e -> {
            currentFrame.dispose();
            new NewProject();
        });
        return logoutButton;
    }

    private Button createBackButton(Frame currentFrame, Runnable onBack) {
        Button backButton = createStyledButton("Back", COLOR_NEUTRAL);
        backButton.addActionListener(e -> {
            currentFrame.dispose();
            onBack.run();
        });
        return backButton;
    }

    // Returns a ready-to-add navigation bar (Back + Logout) for a page.
    // IMPORTANT: call this AFTER the frame's layout has been set to its final
    // BorderLayout, and add the returned panel with a BorderLayout constraint
    // (e.g. BorderLayout.SOUTH) - adding components with no constraint before
    // the layout manager is finalised is what caused the Back/Logout buttons
    // to silently disappear in the previous version of this page.
    private Panel createNavPanel(Frame currentFrame, Runnable onBack) {
        Panel navPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        navPanel.setBackground(COLOR_NAVBAR);
        navPanel.add(createBackButton(currentFrame, onBack));
        navPanel.add(createLogoutButton(currentFrame));
        return navPanel;
    }

    // Builds a simple title bar panel used at the top of the sub-pages.
    private Panel createTitleBar(String text, Color bg) {
        Panel titlePanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 14));
        titlePanel.setBackground(bg);
        Label titleLabel = new Label(text);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    // ── DB helper ─────────────────────────────────────────────────────────────
    private Connection connectDB() {        // This method connects Java to MySQL.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException | ClassNotFoundException ex) {
            showMessage("DB connection error: " + ex.getMessage());
            return null;
        }
    }

    // ── Login check ───────────────────────────────────────────────────────────
    private void checkLogin(String id, String pass) {       // This method checks whether the entered ID and password exist in the database.
        String query = "SELECT * FROM staff_login WHERE id=? AND password=?";
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.setString(2, pass);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {        // Did the database find a matching record?
                    dispose();
                    openSuccessPage();
                } else {
                    showMessage("Invalid ID or Password!");
                }
            }
        } catch (Exception ex) {
            showMessage("Error: " + ex.getMessage());
        }
    }

    // ── Popup message ─────────────────────────────────────────────────────────
    private void showMessage(String msg) {
        Dialog d = new Dialog(this, "Message", true);
        d.setLayout(new FlowLayout());
        d.add(new Label(msg));
        Button ok = new Button("OK");
        ok.addActionListener(e -> d.dispose());
        d.add(ok);
        d.setSize(320, 130);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    // ── Welcome / Success page (Main Window) ────────────────────────────────────
    private void openSuccessPage() {
        Frame successFrame = new Frame("Welcome - Staff Dashboard");
        successFrame.setLayout(new BorderLayout(10, 10));
        successFrame.setBackground(COLOR_BG);

        // Header
        Panel headerPanel = createTitleBar("Welcome, Staff! What would you like to do?", COLOR_HEADER);

        // Action grid (2 rows x 3 columns): create actions on top, view actions below
        Panel actionsPanel = new Panel(new GridLayout(2, 3, 25, 25));
        actionsPanel.setBackground(COLOR_BG);

        Button newInquiryButton    = createStyledButton("+ New Inquiry",  COLOR_PRIMARY);
        Button bookingButton       = createStyledButton("+ New Booking",  COLOR_PRIMARY);
        Button resourcesButton     = createStyledButton("+ Add Resource", COLOR_PRIMARY);
        Button showInquiryButton   = createStyledButton("View Inquiries", COLOR_SECONDARY);
        Button showBookingButton   = createStyledButton("View Bookings",  COLOR_SECONDARY);
        Button showResourcesButton = createStyledButton("View Resources", COLOR_SECONDARY);

        actionsPanel.add(newInquiryButton);
        actionsPanel.add(bookingButton);
        actionsPanel.add(resourcesButton);
        actionsPanel.add(showInquiryButton);
        actionsPanel.add(showBookingButton);
        actionsPanel.add(showResourcesButton);

        Panel centerWrap = new Panel(new FlowLayout(FlowLayout.CENTER, 0, 45));
        centerWrap.setBackground(COLOR_BG);
        centerWrap.add(actionsPanel);

        // Footer - just a Logout button on the main dashboard
        Panel footerPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        footerPanel.setBackground(COLOR_NAVBAR);
        footerPanel.add(createLogoutButton(successFrame));

        successFrame.add(headerPanel, BorderLayout.NORTH);
        successFrame.add(centerWrap, BorderLayout.CENTER);
        successFrame.add(footerPanel, BorderLayout.SOUTH);

        // ── Inquiry Page ──────────────────────────────────────────────────────
        newInquiryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                successFrame.dispose();
                openInquiryPage();
            }
        });

        // ── Booking Page ──────────────────────────────────────────────────────
        bookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                successFrame.dispose();
                openBookingPage();
            }
        });

        // ── Resources Page ────────────────────────────────────────────────────
        resourcesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                successFrame.dispose();
                openResourcesPage();
            }
        });

        // ── show Inquiry Page ──────────────────────────────────────────────────────
        showInquiryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                successFrame.dispose();
                showInquiryPage();
            }
        });

        // ── show Booking Page ──────────────────────────────────────────────────────
        showBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                successFrame.dispose();
                showBookingPage();
            }
        });

        // ── show Resources Page ──────────────────────────────────────────────────────
        showResourcesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                successFrame.dispose();
                showResourcesPage();
            }
        });

        successFrame.setSize(680, 420);
        successFrame.setLocationRelativeTo(null);
        successFrame.setVisible(true);

        successFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                successFrame.dispose();
            }
        });
    }

    // =========================================================================
    // INQUIRY PAGE
    // =========================================================================
    private void openInquiryPage() {

        // ── Frame setup (layout is set immediately so every component we add
        //     below is correctly tracked by this one BorderLayout instance) ──
        Frame inquiryFrame = new Frame("Inquiry Form");
        inquiryFrame.setSize(750, 680);
        inquiryFrame.setLayout(new BorderLayout(10, 10));
        inquiryFrame.setBackground(new Color(240, 248, 255));   // Light blue background

        // ── Title panel ───────────────────────────────────────────────────────
        Panel titlePanel = createTitleBar("Inquiry Form", new Color(30, 80, 160));

        // ── Form panel (rows auto-sized to the number of fields added) ────────
        Panel formPanel = new Panel(new GridLayout(0, 2, 12, 10));
        formPanel.setBackground(new Color(240, 248, 255));

        // Row – Name
        formPanel.add(new Label("Name:"));
        TextField nameField = new TextField();
        formPanel.add(nameField);

        // Row – ContactNo
        formPanel.add(new Label("Contact No:"));
        TextField contactField = new TextField();
        formPanel.add(contactField);

        // Row – Email_ID
        formPanel.add(new Label("Email ID:"));
        TextField emailField = new TextField();
        formPanel.add(emailField);

        // Row – Resource_ID
        formPanel.add(new Label("Resource ID:"));
        TextField resourceField = new TextField();
        formPanel.add(resourceField);

        // Row – StartDate
        formPanel.add(new Label("Start Date (YYYY-MM-DD):"));
        TextField startDateField = new TextField();
        formPanel.add(startDateField);

        // Row – EndDate
        formPanel.add(new Label("End Date (YYYY-MM-DD):"));
        TextField endDateField = new TextField();
        formPanel.add(endDateField);

        // Row – Comment
        formPanel.add(new Label("Comment:"));
        TextField commentField = new TextField();
        formPanel.add(commentField);

        // Row – Action buttons
        Button submitButton = createStyledButton("Submit Inquiry", COLOR_PRIMARY);
        Button clearButton  = createStyledButton("Clear", COLOR_DANGER);
        Button checkAvailability = createStyledButton("Check Availability", COLOR_SECONDARY);
        formPanel.add(submitButton);
        formPanel.add(clearButton);
        formPanel.add(checkAvailability);
        formPanel.add(new Label(""));

        // ── Records area ──────────────────────────────────────────────────────
        Panel recordsPanel = new Panel(new BorderLayout());
        Label recordsLabel = new Label("Inquiry Records:");
        recordsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        TextArea recordsArea = new TextArea(6, 80);
        recordsArea.setEditable(false);
        recordsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        recordsPanel.add(recordsLabel, BorderLayout.NORTH);
        recordsPanel.add(recordsArea,  BorderLayout.CENTER);

        // ── South area: records on top, Back/Logout nav bar below ─────────────
        Panel southPanel = new Panel(new BorderLayout(5, 5));
        southPanel.add(recordsPanel, BorderLayout.CENTER);
        southPanel.add(createNavPanel(inquiryFrame, this::openSuccessPage), BorderLayout.SOUTH);

        // ── Main layout ───────────────────────────────────────────────────────
        inquiryFrame.add(titlePanel,  BorderLayout.NORTH);
        inquiryFrame.add(formPanel,   BorderLayout.CENTER);
        inquiryFrame.add(southPanel,  BorderLayout.SOUTH);

        // ── Submit inquiry – insert into DB ───────────────────────────────────
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                // Read all fields
                String name       = nameField.getText().trim();
                String contact    = contactField.getText().trim();
                String email      = emailField.getText().trim();
                String resourceId = resourceField.getText().trim();
                String startDate  = startDateField.getText().trim();
                String endDate    = endDateField.getText().trim();
                String comment    = commentField.getText().trim();

                // Validate – all fields required
                if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || resourceId.isEmpty() || startDate.isEmpty() || endDate.isEmpty())
                {
                    showMessage("Please fill in all required fields.");
                    return;
                }

                // Validate date format (YYYY-MM-DD)
                if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}")
                        || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    showMessage("Dates must be in YYYY-MM-DD format!");
                    return;
                }

                // ── Check availability against booking table BEFORE inserting ──────────
                String checkQuery = "SELECT * FROM booking WHERE Resource_ID = ? AND StartDate <= ? AND EndDate >= ?";

                try (Connection checkConn = connectDB();
                PreparedStatement checkStmt = checkConn.prepareStatement(checkQuery)) {

                checkStmt.setInt(1, Integer.parseInt(resourceId));
                checkStmt.setDate(2, Date.valueOf(endDate));
                checkStmt.setDate(3, Date.valueOf(startDate));

                try (ResultSet checkRs = checkStmt.executeQuery()) {
                    if (checkRs.next()) {
                        // Conflict found – stop here, do not insert the inquiry
                        showMessage("This room is already booked/full for the selected dates!");
                        inquiryFrame.dispose();
                        openSuccessPage();
                        return;
                    }
                }

            } catch (NumberFormatException ex) {
                showMessage("Resource ID must be a number.");
                return;
            } catch (Exception ex) {
                showMessage("Error checking availability: " + ex.getMessage());
                return;
            }

                // INSERT into Inquiry table
                String query = "INSERT INTO inquiry "
                             + "(Name, ContactNo,Email_ID, ResourceID, StartDate, EndDate, Comment) "
                             + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = connectDB();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, name);
                    stmt.setString(2, contact);
                    stmt.setString(3, email);
                    stmt.setInt(4,    Integer.parseInt(resourceId));
                    stmt.setDate(5,   Date.valueOf(startDate));
                    stmt.setDate(6,   Date.valueOf(endDate));
                    stmt.setString(7, comment);

                    stmt.executeUpdate();
                    showMessage("Inquiry Submitted Successfully!");

                    // Clear all fields after successful save
                    nameField.setText("");     emailField.setText("");
                    contactField.setText("");  resourceField.setText("");
                    startDateField.setText(""); endDateField.setText("");
                    commentField.setText("");
                    // Refresh the records display
                    fetchAndDisplayInquiry(recordsArea);

                } catch (NumberFormatException ex) {
                    showMessage("Resource ID, Inquiry ID and Price must be numbers.");
                } catch (Exception ex) {
                    showMessage("Error: " + ex.getMessage());
                }
            }
        });

        // ── Clear button – reset all fields ───────────────────────────────────
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                nameField.setText("");     emailField.setText("");
                contactField.setText("");  resourceField.setText("");
                startDateField.setText(""); endDateField.setText("");
                commentField.setText("");
            }
        });

        // ── Check Availability ──────────────────────────────────────────────────
        checkAvailability.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                String resourceId = resourceField.getText().trim();
                String startDate  = startDateField.getText().trim();
                String endDate    = endDateField.getText().trim();

                // Validate required fields
                if (resourceId.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    showMessage("Please enter Resource ID, Start Date and End Date to check availability.");
                    return;
                }

                // Validate date format
                if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}") || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    showMessage("Dates must be in YYYY-MM-DD format!");
                    return;
                }

                // Overlap logic: an existing booking conflicts if
                // existing.StartDate <= yourEndDate AND existing.EndDate >= yourStartDate
                String query = "SELECT * FROM booking WHERE Resource_ID = ? AND StartDate <= ? AND EndDate >= ?";

                try (Connection conn = connectDB();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, Integer.parseInt(resourceId));
                    stmt.setDate(2, Date.valueOf(endDate));
                    stmt.setDate(3, Date.valueOf(startDate));

                    try (ResultSet rs = stmt.executeQuery()) {

                        StringBuilder sb = new StringBuilder();
                        boolean found = false;

                        while (rs.next()) {
                            found = true;
                            sb.append("Booking_ID: ").append(rs.getInt("Booking_ID")).append("\t")
                              .append("Resource_ID: ").append(rs.getInt("Resource_ID")).append("\t")
                              .append("StartDate: ").append(rs.getDate("StartDate")).append("\t")
                              .append("EndDate: ").append(rs.getDate("EndDate")).append("\n");
                        }

                        if (found) {
                            recordsArea.setText(sb.toString());
                            showMessage("This room is already booked for the selected dates!");
                        } else {
                            recordsArea.setText("");
                            showMessage("You can book this room! It is available for the selected dates.");
                        }
                    }

                } catch (NumberFormatException ex) {
                    showMessage("Resource ID must be a number.");
                } catch (Exception ex) {
                    showMessage("Error: " + ex.getMessage());
                }
            }
        });

        // Load existing inquiry records on page open
        fetchAndDisplayInquiry(recordsArea);

        inquiryFrame.setVisible(true);

        inquiryFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                inquiryFrame.dispose();
                openSuccessPage();
            }
        });
    }   // <-- closes openInquiryPage()

    // Fetch all rows from inquiry table and show in the TextArea (labeled format)
    private void fetchAndDisplayInquiry(TextArea recordsArea) {
        String query = "SELECT * FROM inquiry ORDER BY Inquiry_ID DESC";
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            recordsArea.setText("");
            recordsArea.append("------------------------- Here are your Latest Records --------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;

                recordsArea.append("====================================================\n");
                recordsArea.append("Inquiry ID : " + rs.getInt("Inquiry_ID") + "\n");
                recordsArea.append("Name       : " + rs.getString("Name") + "\n");
                recordsArea.append("Contact    : " + rs.getString("ContactNo") + "\n");
                recordsArea.append("Email      : " + rs.getString("Email_ID") + "\n");
                recordsArea.append("ResourceID : " + rs.getInt("ResourceID") + "\n");
                recordsArea.append("Start Date : " + rs.getDate("StartDate") + "\n");
                recordsArea.append("End Date   : " + rs.getDate("EndDate") + "\n");
                recordsArea.append("Comment    : " + rs.getString("Comment") + "\n");
            }
            if (!found) recordsArea.append("No records found.\n");

        } catch (Exception e) {
            recordsArea.append("Database error: " + e.getMessage() + "\n");
        }
    }

    // =========================================================================
    // BOOKING PAGE
    // =========================================================================
    private void openBookingPage() {

        // ── Frame setup ───────────────────────────────────────────────────────
        Frame bookingFrame = new Frame("Booking Form");
        bookingFrame.setSize(750, 700);
        bookingFrame.setLayout(new BorderLayout(10, 10));
        bookingFrame.setBackground(new Color(240, 248, 255));   // Light blue background

        // ── Title panel ───────────────────────────────────────────────────────
        Panel titlePanel = createTitleBar("Booking Form", new Color(30, 80, 160));

        // ── Form panel (rows auto-sized to the number of fields added) ────────
        Panel formPanel = new Panel(new GridLayout(0, 2, 12, 8));
        formPanel.setBackground(new Color(240, 248, 255));

        // Row – Name
        formPanel.add(new Label("Name:"));
        TextField nameField = new TextField();
        formPanel.add(nameField);

        // Row – Email_ID
        formPanel.add(new Label("Email ID:"));
        TextField emailField = new TextField();
        formPanel.add(emailField);

        // Row – ContactNo
        formPanel.add(new Label("Contact No:"));
        TextField contactField = new TextField();
        formPanel.add(contactField);

        // Row – Resource_ID
        formPanel.add(new Label("Resource ID:"));
        TextField resourceField = new TextField();
        formPanel.add(resourceField);

        // Row – StartDate
        formPanel.add(new Label("Start Date (YYYY-MM-DD):"));
        TextField startDateField = new TextField();
        formPanel.add(startDateField);

        // Row – EndDate
        formPanel.add(new Label("End Date (YYYY-MM-DD):"));
        TextField endDateField = new TextField();
        formPanel.add(endDateField);

        // Row – Comment
        formPanel.add(new Label("Comment:"));
        TextField commentField = new TextField();
        formPanel.add(commentField);

        // Row – Inquiry_ID + a button to auto-fill the form from that inquiry
        formPanel.add(new Label("Inquiry ID:"));
        TextField inquiryField = new TextField();
        formPanel.add(inquiryField);

        Button loadInquiryButton = createStyledButton("Load From Inquiry ID", COLOR_SECONDARY);
        formPanel.add(new Label(""));
        formPanel.add(loadInquiryButton);

        // Row – price
        formPanel.add(new Label("Price:"));
        TextField priceField = new TextField();
        formPanel.add(priceField);

        // Row – Action buttons
        Button submitButton = createStyledButton("Submit Booking", COLOR_PRIMARY);
        Button clearButton  = createStyledButton("Clear", COLOR_DANGER);
        formPanel.add(submitButton);
        formPanel.add(clearButton);

        // ── Records area ──────────────────────────────────────────────────────
        Panel recordsPanel = new Panel(new BorderLayout());
        Label recordsLabel = new Label("Booking Records:");
        recordsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        TextArea recordsArea = new TextArea(6, 80);
        recordsArea.setEditable(false);
        recordsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        recordsPanel.add(recordsLabel, BorderLayout.NORTH);
        recordsPanel.add(recordsArea,  BorderLayout.CENTER);

        // ── South area: records on top, Back/Logout nav bar below ─────────────
        Panel southPanel = new Panel(new BorderLayout(5, 5));
        southPanel.add(recordsPanel, BorderLayout.CENTER);
        southPanel.add(createNavPanel(bookingFrame, this::openSuccessPage), BorderLayout.SOUTH);

        // ── Main layout ───────────────────────────────────────────────────────
        bookingFrame.add(titlePanel,  BorderLayout.NORTH);
        bookingFrame.add(formPanel,   BorderLayout.CENTER);
        bookingFrame.add(southPanel,  BorderLayout.SOUTH);

        // ── Load From Inquiry ID – auto-fill the booking form from an inquiry ──
        loadInquiryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String inquiryId = inquiryField.getText().trim();
                if (inquiryId.isEmpty()) {
                    showMessage("Enter an Inquiry ID first.");
                    return;
                }

                String query = "SELECT * FROM inquiry WHERE Inquiry_ID = ?";
                try (Connection conn = connectDB();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, Integer.parseInt(inquiryId));

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            nameField.setText(rs.getString("Name"));
                            emailField.setText(rs.getString("Email_ID"));
                            contactField.setText(rs.getString("ContactNo"));
                            resourceField.setText(String.valueOf(rs.getInt("ResourceID")));
                            startDateField.setText(rs.getDate("StartDate").toString());
                            endDateField.setText(rs.getDate("EndDate").toString());
                            commentField.setText(rs.getString("Comment"));
                            showMessage("Inquiry details loaded. Review and submit the booking.");
                        } else {
                            showMessage("No inquiry found with that ID.");
                        }
                    }

                } catch (NumberFormatException ex) {
                    showMessage("Inquiry ID must be a number.");
                } catch (Exception ex) {
                    showMessage("Error: " + ex.getMessage());
                }
            }
        });

        // ── Submit booking – insert into DB ───────────────────────────────────
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                // Read all fields
                String name       = nameField.getText().trim();
                String email      = emailField.getText().trim();
                String contact    = contactField.getText().trim();
                String resourceId = resourceField.getText().trim();
                String startDate  = startDateField.getText().trim();
                String endDate    = endDateField.getText().trim();
                String comment    = commentField.getText().trim();
                String inquiryId  = inquiryField.getText().trim();
                String price      = priceField.getText().trim();

                // Validate – all fields required
                if (name.isEmpty() || email.isEmpty() || contact.isEmpty()
                        || resourceId.isEmpty() || startDate.isEmpty()
                        || endDate.isEmpty() || inquiryId.isEmpty() || price.isEmpty()) {
                    showMessage("Please fill in all required fields.");
                    return;
                }

                // Validate date format (YYYY-MM-DD)
                if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}")
                        || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    showMessage("Dates must be in YYYY-MM-DD format!");
                    return;
                }

                // Make sure the Inquiry ID actually exists before booking against it
                String inquiryCheckQuery = "SELECT * FROM inquiry WHERE Inquiry_ID = ?";
                try (Connection checkConn = connectDB();
                     PreparedStatement checkStmt = checkConn.prepareStatement(inquiryCheckQuery)) {

                    checkStmt.setInt(1, Integer.parseInt(inquiryId));
                    try (ResultSet checkRs = checkStmt.executeQuery()) {
                        if (!checkRs.next()) {
                            showMessage("No inquiry found with Inquiry ID " + inquiryId + ". Please check the ID.");
                            return;
                        }
                    }
                } catch (NumberFormatException ex) {
                    showMessage("Inquiry ID must be a number.");
                    return;
                } catch (Exception ex) {
                    showMessage("Error validating Inquiry ID: " + ex.getMessage());
                    return;
                }

                // INSERT into booking table
                String query = "INSERT INTO booking "
                             + "(Name, Email_ID, ContactNo, Resource_ID, StartDate, EndDate, Comment, Inquiry_ID, price) "
                             + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = connectDB();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, contact);
                    stmt.setInt(4,    Integer.parseInt(resourceId));
                    stmt.setDate(5,   Date.valueOf(startDate));
                    stmt.setDate(6,   Date.valueOf(endDate));
                    stmt.setString(7, comment);
                    stmt.setInt(8,    Integer.parseInt(inquiryId));
                    stmt.setInt(9,    Integer.parseInt(price));

                    stmt.executeUpdate();
                    showMessage("Booking Submitted Successfully!");

                    // Clear all fields after successful save
                    nameField.setText("");     emailField.setText("");
                    contactField.setText("");  resourceField.setText("");
                    startDateField.setText(""); endDateField.setText("");
                    commentField.setText("");  inquiryField.setText("");
                    priceField.setText("");

                    // Refresh the records display
                    fetchAndDisplayBookings(recordsArea);

                } catch (NumberFormatException ex) {
                    showMessage("Resource ID, Inquiry ID and Price must be numbers.");
                } catch (Exception ex) {
                    showMessage("Error: " + ex.getMessage());
                }
            }
        });

        // ── Clear button – reset all fields ───────────────────────────────────
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                nameField.setText("");     emailField.setText("");
                contactField.setText("");  resourceField.setText("");
                startDateField.setText(""); endDateField.setText("");
                commentField.setText("");  inquiryField.setText("");
                priceField.setText("");
            }
        });

        // Load existing booking records on page open
        fetchAndDisplayBookings(recordsArea);

        bookingFrame.setVisible(true);

        bookingFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                bookingFrame.dispose();
                openSuccessPage();
            }
        });
    }

    // Fetch all rows from booking table and show in the TextArea
    private void fetchAndDisplayBookings(TextArea recordsArea) {
        String query = "SELECT * FROM booking";
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            sb.append("Booking_ID\tName\tEmail_ID\tContactNo\tResource_ID\tStartDate\tEndDate\tComment\tInquiry_ID\tPrice\n");
            sb.append("---------------------------------------------------------------------------------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append(rs.getInt("Booking_ID"))      .append("\t")
                  .append(rs.getString("Name"))          .append("\t")
                  .append(rs.getString("Email_ID"))      .append("\t")
                  .append(rs.getString("ContactNo"))     .append("\t")
                  .append(rs.getInt("Resource_ID"))      .append("\t")
                  .append(rs.getDate("StartDate"))       .append("\t")
                  .append(rs.getDate("EndDate"))         .append("\t")
                  .append(rs.getString("Comment"))       .append("\t")
                  .append(rs.getInt("Inquiry_ID"))       .append("\t")
                  .append(rs.getInt("price"))            .append("\n");
            }
            if (!found) sb.append("No bookings found.\n");
            recordsArea.setText(sb.toString());

        } catch (Exception e) {
            recordsArea.append("Database error: " + e.getMessage() + "\n");
        }
    }

    // =========================================================================
    // RESOURCES PAGE
    // =========================================================================
    private void openResourcesPage() {

        // ── Frame setup ───────────────────────────────────────────────────────
        Frame resourcesFrame = new Frame("Resources Form");
        resourcesFrame.setSize(800, 650);
        resourcesFrame.setLayout(new BorderLayout(10, 10));
        resourcesFrame.setBackground(new Color(245, 245, 230));     // Warm off-white background

        // ── Title panel ───────────────────────────────────────────────────────
        Panel titlePanel = createTitleBar("Resources Management", new Color(80, 40, 120));

        // ── Form panel (rows auto-sized to the number of fields added) ────────
        Panel formPanel = new Panel(new GridLayout(0, 2, 12, 10));
        formPanel.setBackground(new Color(245, 245, 230));

        // Row – Room_Name
        formPanel.add(new Label("Room Name:"));
        TextField roomNameField = new TextField();
        formPanel.add(roomNameField);

        // Row – Capacity
        formPanel.add(new Label("Capacity:"));
        TextField capacityField = new TextField();
        formPanel.add(capacityField);

        // Row – Features
        formPanel.add(new Label("Features:"));
        TextField featuresField = new TextField();
        formPanel.add(featuresField);

        // Row – Price
        formPanel.add(new Label("Price:"));
        TextField priceField = new TextField();
        formPanel.add(priceField);

        // Row – Action buttons
        Button submitButton = createStyledButton("Add Resource", COLOR_PRIMARY);
        Button clearButton  = createStyledButton("Clear", COLOR_DANGER);
        formPanel.add(submitButton);
        formPanel.add(clearButton);

        // ── Records area ──────────────────────────────────────────────────────
        Panel recordsPanel = new Panel(new BorderLayout());
        Label recordsLabel = new Label("Existing Resources:");
        recordsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        TextArea recordsArea = new TextArea(9, 80);
        recordsArea.setEditable(false);
        recordsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        recordsPanel.add(recordsLabel, BorderLayout.NORTH);
        recordsPanel.add(recordsArea,  BorderLayout.CENTER);

        // ── South area: records on top, Back/Logout nav bar below ─────────────
        Panel southPanel = new Panel(new BorderLayout(5, 5));
        southPanel.add(recordsPanel, BorderLayout.CENTER);
        southPanel.add(createNavPanel(resourcesFrame, this::openSuccessPage), BorderLayout.SOUTH);

        // ── Main layout ───────────────────────────────────────────────────────
        resourcesFrame.add(titlePanel,  BorderLayout.NORTH);
        resourcesFrame.add(formPanel,   BorderLayout.CENTER);
        resourcesFrame.add(southPanel,  BorderLayout.SOUTH);

        // ── Add Resource – insert into DB ─────────────────────────────────────
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                // Read all fields
                String roomName = roomNameField.getText().trim();
                String capacity = capacityField.getText().trim();
                String features = featuresField.getText().trim();
                String price    = priceField.getText().trim();

                // Validate – all fields required
                if (roomName.isEmpty() || capacity.isEmpty() || features.isEmpty() || price.isEmpty()) {
                    showMessage("Please fill in all required fields.");
                    return;
                }

                // INSERT into resources table
                String query = "INSERT INTO resources (Room_Name, Capacity, Features, Price) VALUES (?, ?, ?, ?)";

                try (Connection conn = connectDB();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, roomName);
                    stmt.setInt(2,    Integer.parseInt(capacity));
                    stmt.setString(3, features);
                    stmt.setInt(4,    Integer.parseInt(price));
                    stmt.executeUpdate();

                    showMessage("Resource Added Successfully!");

                    // Clear all fields after successful save
                    roomNameField.setText("");
                    capacityField.setText("");
                    featuresField.setText("");
                    priceField.setText("");

                    // Refresh the records display
                    fetchAndDisplayResources(recordsArea);

                } catch (NumberFormatException ex) {
                    showMessage("Capacity and Price must be numbers.");
                } catch (Exception ex) {
                    showMessage("Error: " + ex.getMessage());
                }
            }
        });

        // ── Clear button – reset all fields ───────────────────────────────────
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                roomNameField.setText("");
                capacityField.setText("");
                featuresField.setText("");
                priceField.setText("");
            }
        });

        // Load existing resources on page open
        fetchAndDisplayResources(recordsArea);

        resourcesFrame.setVisible(true);

        resourcesFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                resourcesFrame.dispose();
                openSuccessPage();
            }
        });
    }

    private void fetchAndDisplayResources(TextArea recordsArea) {
        String query = "SELECT * FROM resources";
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            sb.append("ResourceID\tRoom Name\tCapacity\tFeatures\tPrice\n");
            sb.append("------------------------------------------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append(rs.getInt("ResourceID"))      .append("\t")
                  .append(rs.getString("Room_Name"))    .append("\t")
                  .append(rs.getInt("Capacity"))        .append("\t")
                  .append(rs.getString("Features"))     .append("\t")
                  .append(rs.getInt("Price"))           .append("\n");
            }
            if (!found) sb.append("No resources found.\n");
            recordsArea.setText(sb.toString());

        } catch (Exception e) {
            recordsArea.append("Database error: " + e.getMessage() + "\n");
        }
    }

    // =========================================================================
    // show INQUIRY PAGE
    // =========================================================================
    private void showInquiryPage() {

        Frame frame = new Frame("Show Inquiry");
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(15, 15));
        frame.setBackground(Color.lightGray);

        Panel titlePanel = createTitleBar("Inquiry Records", new Color(30, 80, 160));

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Extra actions (Open / Closed) sit alongside Back/Logout
        Panel navPanel = createNavPanel(frame, this::openSuccessPage);
        Button openInquiriesButton   = createStyledButton("Open Inquiry", COLOR_SECONDARY);
        Button closedInquiriesButton = createStyledButton("Closed Inquiry", COLOR_SECONDARY);
        navPanel.add(openInquiriesButton);
        navPanel.add(closedInquiriesButton);

        // When "Open Inquiry" is clicked, go to the page showing inquiries with no booking yet
        openInquiriesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showOpenInquiries();
            }
        });
        // When "Closed Inquiry" is clicked, go to the page showing inquiries that already have a booking
        closedInquiriesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showClosedInquiries();
            }
        });

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(outputArea, BorderLayout.CENTER);
        frame.add(navPanel, BorderLayout.SOUTH);   // Use the real navPanel (with Open/Closed buttons) here

        fetchAndDisplayshowInquiries(outputArea);

        frame.setVisible(true);
    }

    // The inquiry table's actual column is "ResourceID" (no underscore),
    // unlike the booking table which uses "Resource_ID".
    private void fetchAndDisplayshowInquiries(TextArea outputArea) {
        String query = "SELECT * FROM inquiry ORDER BY Inquiry_ID DESC";
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            outputArea.setText("");
            outputArea.append("------------------------- Here are your Latest Records --------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;

                outputArea.append("====================================================\n");
                outputArea.append("Inquiry ID : " + rs.getInt("Inquiry_ID") + "\n");
                outputArea.append("Name       : " + rs.getString("Name") + "\n");
                outputArea.append("Contact    : " + rs.getString("ContactNo") + "\n");
                outputArea.append("Email      : " + rs.getString("Email_ID") + "\n");
                outputArea.append("ResourceID : " + rs.getInt("ResourceID") + "\n");
                outputArea.append("Start Date : " + rs.getDate("StartDate") + "\n");
                outputArea.append("End Date   : " + rs.getDate("EndDate") + "\n");
                outputArea.append("Comment    : " + rs.getString("Comment") + "\n");
            }
            if (!found) outputArea.append("No records found.\n");

        } catch (Exception e) {
            outputArea.append("Database error: " + e.getMessage() + "\n");
        }
    }

    // =========================================================================
    // OPEN INQUIRIES – inquiries that do NOT yet have a matching booking
    // =========================================================================
    private void showOpenInquiries() {

        Frame frame = new Frame("Open Inquiries");
        frame.setSize(800, 550);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setBackground(Color.lightGray);

        Panel titlePanel = createTitleBar("Open Inquiries", new Color(80, 40, 120));

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(area, BorderLayout.CENTER);
        frame.add(createNavPanel(frame, this::showInquiryPage), BorderLayout.SOUTH);

        // An inquiry is "open" if its Inquiry_ID has never been turned into a booking.
        String query = "SELECT * FROM inquiry "
                      + "WHERE Inquiry_ID NOT IN (SELECT DISTINCT Inquiry_ID FROM booking) "
                      + "ORDER BY Inquiry_ID DESC";

        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            area.append("------------------------- Latest Open Inquiries --------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                area.append("====================================================\n");
                area.append("Inquiry ID : " + rs.getInt("Inquiry_ID") + "\n");
                area.append("Name       : " + rs.getString("Name") + "\n");
                area.append("Contact    : " + rs.getString("ContactNo") + "\n");
                area.append("Email      : " + rs.getString("Email_ID") + "\n");
                area.append("ResourceID : " + rs.getInt("ResourceID") + "\n");
                area.append("Start Date : " + rs.getDate("StartDate") + "\n");
                area.append("End Date   : " + rs.getDate("EndDate") + "\n");
                area.append("Comment    : " + rs.getString("Comment") + "\n");
            }
            if (!found) area.append("No open inquiries right now.\n");

        } catch (Exception ex) {
            area.setText("Database error: " + ex.getMessage());
        }
        frame.setVisible(true);
    }

    // =========================================================================
    // CLOSED INQUIRIES – inquiries that already have a matching booking
    // =========================================================================
    private void showClosedInquiries() {

        Frame frame = new Frame("Closed Inquiries");
        frame.setSize(800, 550);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setBackground(Color.lightGray);

        Panel titlePanel = createTitleBar("Closed Inquiries", new Color(80, 40, 120));

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(area, BorderLayout.CENTER);
        frame.add(createNavPanel(frame, this::showInquiryPage), BorderLayout.SOUTH);

        // An inquiry is "closed" once a booking exists for that Inquiry_ID.
        String query = "SELECT i.Inquiry_ID, i.Name, i.ContactNo, i.Email_ID, i.ResourceID, "
                      + "i.StartDate, i.EndDate, i.Comment, b.Booking_ID, b.price "
                      + "FROM inquiry i "
                      + "INNER JOIN booking b ON i.Inquiry_ID = b.Inquiry_ID "
                      + "ORDER BY i.Inquiry_ID DESC";

        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            area.append("------------------------- Closed Inquiries --------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                area.append("====================================================\n");
                area.append("Inquiry ID : " + rs.getInt("Inquiry_ID") + "\n");
                area.append("Name       : " + rs.getString("Name") + "\n");
                area.append("Contact    : " + rs.getString("ContactNo") + "\n");
                area.append("Email      : " + rs.getString("Email_ID") + "\n");
                area.append("ResourceID : " + rs.getInt("ResourceID") + "\n");
                area.append("Start Date : " + rs.getDate("StartDate") + "\n");
                area.append("End Date   : " + rs.getDate("EndDate") + "\n");
                area.append("Comment    : " + rs.getString("Comment") + "\n");
                area.append("Booking ID : " + rs.getInt("Booking_ID") + "\n");
                area.append("Price      : " + rs.getInt("price") + "\n");
            }
            if (!found) area.append("No closed inquiries yet.\n");

        } catch (Exception ex) {
            area.setText("Database error: " + ex.getMessage());
        }
        frame.setVisible(true);
    }

    // =========================================================================
    // show Booking PAGE
    // =========================================================================
    private void showBookingPage() {

        Frame frame = new Frame("Show Booking");
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(15, 15));
        frame.setBackground(Color.lightGray);

        Panel titlePanel = createTitleBar("Booking Records", new Color(30, 80, 160));

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Extra action (Search Booking) sits alongside Back/Logout
        Panel navPanel = createNavPanel(frame, this::openSuccessPage);
        Button searchBookingButton = createStyledButton("Search Booking", COLOR_SECONDARY);
        navPanel.add(searchBookingButton);

        // When "Search Booking" is clicked, ask the user for a date and show matching bookings
        searchBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                promptDateAndSearchBookings(frame, outputArea);
            }
        });

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(outputArea, BorderLayout.CENTER);
        frame.add(navPanel, BorderLayout.SOUTH);

        fetchAndDisplayshowbooking(outputArea);

        frame.setVisible(true);
    }

    // Loads and displays every booking record, most recent first.
    private void fetchAndDisplayshowbooking(TextArea outputArea) {
        String query = "SELECT * FROM booking ORDER BY Booking_ID DESC";
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            outputArea.setText("");
            outputArea.append("------------------------- Here are your Latest Records --------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;

                outputArea.append("====================================================\n");
                outputArea.append("Booking ID : " + rs.getInt("Booking_ID") + "\n");
                outputArea.append("Name       : " + rs.getString("Name") + "\n");
                outputArea.append("Contact    : " + rs.getString("ContactNo") + "\n");
                outputArea.append("Email      : " + rs.getString("Email_ID") + "\n");
                outputArea.append("ResourceID : " + rs.getInt("Resource_ID") + "\n");
                outputArea.append("Start Date : " + rs.getDate("StartDate") + "\n");
                outputArea.append("End Date   : " + rs.getDate("EndDate") + "\n");
                outputArea.append("Comment    : " + rs.getString("Comment") + "\n");
                outputArea.append("Inquiry ID : " + rs.getInt("Inquiry_ID") + "\n");
                outputArea.append("Price      : " + rs.getInt("Price") + "\n");
            }
            if (!found) outputArea.append("No records found.\n");

        } catch (Exception e) {
            outputArea.append("Database error: " + e.getMessage() + "\n");
        }
    }

    // Small popup dialog that asks the user for a date, then triggers the search.
    private void promptDateAndSearchBookings(Frame parent, TextArea outputArea) {
        Dialog d = new Dialog(parent, "Search Booking", true);
        d.setLayout(new FlowLayout());

        d.add(new Label("Enter Date (YYYY-MM-DD):"));
        TextField dateField = new TextField(12);
        d.add(dateField);

        // Search button - validates the date then runs the query
        Button searchBtn = new Button("Search");
        searchBtn.addActionListener(e -> {
            String date = dateField.getText().trim();

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                showMessage("Date must be in YYYY-MM-DD format!");
                return;
            }

            d.dispose();
            searchBookingsByDate(date, outputArea);
        });
        d.add(searchBtn);

        // Cancel button - just closes the popup, does nothing else
        Button cancelBtn = new Button("Cancel");
        cancelBtn.addActionListener(e -> d.dispose());
        d.add(cancelBtn);

        d.setSize(320, 140);
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }

    // Shows bookings whose date range (StartDate..EndDate) covers the given date.
    private void searchBookingsByDate(String date, TextArea outputArea) {
        String query = "SELECT * FROM booking WHERE ? BETWEEN StartDate AND EndDate ORDER BY Booking_ID DESC";

        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                outputArea.setText("");
                outputArea.append("------------------------- Bookings on " + date + " --------------------\n");

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    outputArea.append("====================================================\n");
                    outputArea.append("Booking ID : " + rs.getInt("Booking_ID") + "\n");
                    outputArea.append("Name       : " + rs.getString("Name") + "\n");
                    outputArea.append("Contact    : " + rs.getString("ContactNo") + "\n");
                    outputArea.append("Email      : " + rs.getString("Email_ID") + "\n");
                    outputArea.append("ResourceID : " + rs.getInt("Resource_ID") + "\n");
                    outputArea.append("Start Date : " + rs.getDate("StartDate") + "\n");
                    outputArea.append("End Date   : " + rs.getDate("EndDate") + "\n");
                    outputArea.append("Comment    : " + rs.getString("Comment") + "\n");
                    outputArea.append("Inquiry ID : " + rs.getInt("Inquiry_ID") + "\n");
                    outputArea.append("Price      : " + rs.getInt("price") + "\n");
                }
                if (!found) outputArea.append("No bookings found for this date.\n");
            }

        } catch (IllegalArgumentException ex) {
            outputArea.setText("Invalid date value: " + date);
        } catch (Exception ex) {
            outputArea.setText("Database error: " + ex.getMessage());
        }
    }

    // =========================================================================
    // show Resource PAGE
    // =========================================================================
    private void showResourcesPage() {

        Frame frame = new Frame("Show Resources");
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(15, 15));
        frame.setBackground(Color.lightGray);

        Panel titlePanel = createTitleBar("Resource Records", new Color(80, 40, 120));

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Extra actions (Available Rooms / Booked Rooms) sit alongside Back/Logout
        Panel navPanel = createNavPanel(frame, this::openSuccessPage);
        Button availableRoomsButton = createStyledButton("Available Rooms", COLOR_SECONDARY);
        Button bookedRoomsButton    = createStyledButton("Booked Rooms", COLOR_SECONDARY);
        navPanel.add(availableRoomsButton);
        navPanel.add(bookedRoomsButton);

        availableRoomsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showAvailableRooms();
            }
        });
        bookedRoomsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showBookedRooms();
            }
        });

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(outputArea, BorderLayout.CENTER);
        frame.add(navPanel, BorderLayout.SOUTH);

        fetchAndDisplayshowResources(outputArea);

        frame.setVisible(true);
    }

    private void fetchAndDisplayshowResources(TextArea outputArea) {
        String query = "SELECT * FROM resources ORDER BY ResourceID DESC";
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            outputArea.setText("");
            outputArea.append("------------------------- Here are your Latest Records --------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;

                outputArea.append("====================================================\n");
                outputArea.append("Resource ID     : " + rs.getInt("ResourceID") + "\n");
                outputArea.append("Room_Name       : " + rs.getString("Room_Name") + "\n");
                outputArea.append("Capacity        : " + rs.getString("Capacity") + "\n");
                outputArea.append("Features        : " + rs.getString("Features") + "\n");
                outputArea.append("Price           : " + rs.getInt("Price") + "\n");
            }
            if (!found) outputArea.append("No records found.\n");

        } catch (Exception e) {
            outputArea.append("Database error: " + e.getMessage() + "\n");
        }
    }

    // =========================================================================
    // AVAILABLE ROOMS – resources that currently have NO booking record at all
    // =========================================================================
    private void showAvailableRooms() {

        Frame frame = new Frame("Available Resources");
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.lightGray);

        Panel titlePanel = createTitleBar("Available Resources", new Color(80, 40, 120));

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(area, BorderLayout.CENTER);
        frame.add(createNavPanel(frame, this::showResourcesPage), BorderLayout.SOUTH);

        // A resource counts as "available" here if its ID never shows up
        // in the booking table at all (i.e. it has never been booked).
        String query = "SELECT * FROM resources "
                      + "WHERE ResourceID NOT IN (SELECT DISTINCT Resource_ID FROM booking)";

        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            area.append("ID\tRoom Name\tCapacity\tFeatures\tPrice\n");
            area.append("-------------------------------------------------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                area.append(
                    rs.getInt("ResourceID") + "\t" +
                    rs.getString("Room_Name") + "\t" +
                    rs.getInt("Capacity") + "\t" +
                    rs.getString("Features") + "\t" +
                    rs.getInt("Price") + "\n"
                );
            }
            if (!found) area.append("No available rooms right now.\n");

        } catch (Exception ex) {
            area.setText("Database error: " + ex.getMessage());
        }
        frame.setVisible(true);
    }

    // =========================================================================
    // BOOKED ROOMS – which resource is booked, and by whom
    // =========================================================================
    private void showBookedRooms() {

        Frame frame = new Frame("Booked Rooms");
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.lightGray);

        Panel titlePanel = createTitleBar("Booked Rooms", new Color(80, 40, 120));

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(area, BorderLayout.CENTER);
        frame.add(createNavPanel(frame, this::showResourcesPage), BorderLayout.SOUTH);

        // Joins booking to resources so we can show which room (resource)
        // is booked and who booked it.
        String query = "SELECT b.Booking_ID, b.Name, b.Email_ID, b.ContactNo, "
                      + "r.ResourceID, r.Room_Name, r.Capacity, r.Features, r.Price, "
                      + "b.StartDate, b.EndDate, b.Comment "
                      + "FROM booking b "
                      + "INNER JOIN resources r ON b.Resource_ID = r.ResourceID "
                      + "ORDER BY b.Booking_ID DESC";

        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            area.append("ID\tRoom Name\tCapacity\tFeatures\tPrice\n");
            area.append("-------------------------------------------------------------\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                area.append(
                    "Booking ID : " + rs.getInt("Booking_ID") + "\n" +
                    "Booked By  : " + rs.getString("Name") + "\n" +
                    "Email      : " + rs.getString("Email_ID") + "\n" +
                    "Contact    : " + rs.getString("ContactNo") + "\n" +
                    "Room ID    : " + rs.getInt("ResourceID") + "\n" +
                    "Room Name  : " + rs.getString("Room_Name") + "\n" +
                    "Capacity   : " + rs.getInt("Capacity") + "\n" +
                    "Features   : " + rs.getString("Features") + "\n" +
                    "Price      : " + rs.getInt("Price") + "\n" +
                    "Start Date : " + rs.getDate("StartDate") + "\n" +
                    "End Date   : " + rs.getDate("EndDate") + "\n" +
                    "Comment    : " + rs.getString("Comment") + "\n" +
                    "=====================================================\n"
                );
            }
            if (!found) area.append("No booked rooms found.\n");

        } catch (Exception ex) {
            area.setText("Database error: " + ex.getMessage());
        }
        frame.setVisible(true);
    }

    // ── Main ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        new NewProject();
    }
}