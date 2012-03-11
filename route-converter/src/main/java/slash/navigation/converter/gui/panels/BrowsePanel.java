/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/

package slash.navigation.converter.gui.panels;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import slash.common.io.Files;
import slash.navigation.babel.BabelException;
import slash.navigation.base.BaseNavigationFormat;
import slash.navigation.base.BaseNavigationPosition;
import slash.navigation.base.BaseRoute;
import slash.navigation.base.NavigationFileParser;
import slash.navigation.catalog.domain.Catalog;
import slash.navigation.catalog.domain.Route;
import slash.navigation.catalog.local.LocalCatalog;
import slash.navigation.catalog.model.CategoryTreeModel;
import slash.navigation.catalog.model.CategoryTreeNode;
import slash.navigation.catalog.model.CategoryTreeNodeImpl;
import slash.navigation.catalog.model.RootTreeNode;
import slash.navigation.catalog.model.RoutesListModel;
import slash.navigation.catalog.remote.RemoteCatalog;
import slash.navigation.converter.gui.RouteConverter;
import slash.navigation.converter.gui.dialogs.AddFileDialog;
import slash.navigation.converter.gui.dialogs.AddUrlDialog;
import slash.navigation.converter.gui.dnd.CategorySelection;
import slash.navigation.converter.gui.dnd.DnDHelper;
import slash.navigation.converter.gui.dnd.RouteSelection;
import slash.navigation.converter.gui.helper.RouteServiceOperator;
import slash.navigation.converter.gui.helper.TreePathStringConversion;
import slash.navigation.converter.gui.renderer.CategoryTreeCellRenderer;
import slash.navigation.converter.gui.renderer.RoutesTableCellHeaderRenderer;
import slash.navigation.converter.gui.renderer.RoutesTableCellRenderer;
import slash.navigation.gui.Constants;
import slash.navigation.gui.FrameAction;
import slash.navigation.util.RouteComments;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import static java.util.Arrays.asList;
import static slash.common.io.Transfer.trim;

/**
 * The browse panel of the route converter user interface.
 *
 * @author Christian Pesch
 */

public class BrowsePanel {
    private static final Logger log = Logger.getLogger(BrowsePanel.class.getName());
    private static final Preferences preferences = Preferences.userNodeForPackage(RouteConverter.class);

    private static final String LOCAL_CATALOG_ROOT_FOLDER_PREFERENCE = "localCatalogRootFolder";

    private JPanel browsePanel;
    private JTree treeCategories;
    private JTable tableRoutes;
    private JButton buttonDeleteCategory;
    private JButton buttonAddCategory;
    private JButton buttonRenameCategory;
    private JButton buttonAddFile;
    private JButton buttonAddUrl;
    private JButton buttonRenameRoute;
    private JButton buttonDeleteRoute;
    private JButton buttonLogin;

    private final Catalog remoteCatalog = new RemoteCatalog(System.getProperty("catalog", "http://www.routeconverter.com/catalog/"), RouteConverter.getInstance().getCredentials());
    private final Catalog localCatalog = new LocalCatalog(System.getProperty("root", createRootFolder()));

    public BrowsePanel() {
        initialize();
    }

    private void initialize() {
        final RouteConverter r = RouteConverter.getInstance();

        buttonAddCategory.addActionListener(new FrameAction() {
            public void run() {
                addCategory();
            }
        });

        buttonRenameCategory.addActionListener(new FrameAction() {
            public void run() {
                renameCategory();
            }
        });

        buttonDeleteCategory.addActionListener(new FrameAction() {
            public void run() {
                deleteCategory();
            }
        });

        buttonAddFile.addActionListener(new FrameAction() {
            public void run() {
                addFileToCatalog();
            }
        });

        buttonAddUrl.addActionListener(new FrameAction() {
            public void run() {
                addUrlToCatalog(getSelectedTreeNode(), "");
            }
        });

        buttonRenameRoute.addActionListener(new FrameAction() {
            public void run() {
                renameRoute();
            }
        });

        buttonDeleteRoute.addActionListener(new FrameAction() {
            public void run() {
                deleteRoute();
            }
        });

        buttonLogin.addActionListener(new FrameAction() {
            public void run() {
                getOperator().showLogin();
            }
        });

        treeCategories.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        treeCategories.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                selectTreePath(e.getPath());
            }
        });
        treeCategories.getModel().addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(TreeModelEvent e) {
                selectTreePath(treeCategories.getSelectionModel().getSelectionPath());
            }

            public void treeNodesInserted(TreeModelEvent e) {
            }

            public void treeNodesRemoved(TreeModelEvent e) {
            }

            public void treeStructureChanged(TreeModelEvent e) {
            }
        });
        treeCategories.setCellRenderer(new CategoryTreeCellRenderer());
        treeCategories.setDragEnabled(true);
        treeCategories.setDropMode(DropMode.ON);
        treeCategories.setTransferHandler(new TreeDragAndDropHandler());

        tableRoutes.setDefaultRenderer(Object.class, new RoutesTableCellRenderer());
        tableRoutes.setDragEnabled(true);
        tableRoutes.setTransferHandler(new TableDragHandler());
        tableRoutes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int[] selectedRows = tableRoutes.getSelectedRows();
                if (e.getValueIsAdjusting() || selectedRows.length == 0)
                    return;

                Route route = getRoutesListModel().getRoute(selectedRows[0]);
                URL url;
                try {
                    url = route.getDataUrl();
                    if (url == null)
                        return;
                } catch (Throwable t) {
                    getOperator().handleServiceError(t);
                    return;
                }

                r.openPositionList(asList(url));
            }
        });

        new Thread(new Runnable() {
            public void run() {
                final CategoryTreeNode localRoot = new CategoryTreeNodeImpl(localCatalog.getRootCategory(), true, false);
                final CategoryTreeNodeImpl remoteRoot = new CategoryTreeNodeImpl(remoteCatalog.getRootCategory(), false, true);
                final RootTreeNode root = new RootTreeNode(localRoot, remoteRoot);
                final CategoryTreeModel categoryTreeModel = new CategoryTreeModel(root);
                // do the loading in a separate thread since treeCategories.setModel(categoryTreeModel)
                // would do it in the AWT EventQueue
                categoryTreeModel.getChildCount(remoteRoot);

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Constants.startWaitCursor(r.getFrame().getRootPane());
                        try {
                            treeCategories.setModel(categoryTreeModel);
                            // start with showing the folders below the roots
                            treeCategories.expandRow(0);
                            treeCategories.expandRow(1);
                            String selected = r.getCategoryPreference();
                            selectTreePath(TreePathStringConversion.fromString(root, selected));
                        } finally {
                            Constants.stopWaitCursor(r.getFrame().getRootPane());
                        }
                    }
                });
            }
        }, "CategoryTreeInitializer").start();
    }

    private String createRootFolder() {
        String rootFolderPreference = preferences.get(LOCAL_CATALOG_ROOT_FOLDER_PREFERENCE,
                new File(System.getProperty("user.home"), RouteConverter.getBundle().getString("local-catalog-my-routes")).getAbsolutePath());
        File rootFolder = new File(rootFolderPreference);
        if (!rootFolder.exists()) {
            if (!rootFolder.mkdirs()) {
                log.severe("Cannot create local catalog root folder " + rootFolder);
                getOperator().handleServiceError(new FileNotFoundException(rootFolder.getAbsolutePath()));
            }
        }
        return rootFolder.getAbsolutePath();
    }

    public Component getRootComponent() {
        return browsePanel;
    }

    public JButton getDefaultButton() {
        return buttonAddFile;
    }

    protected CategoryTreeNode getSelectedTreeNode() {
        TreePath treePath = treeCategories.getSelectionPath();
        Object treeNode = treePath != null ?
                treePath.getLastPathComponent() : treeCategories.getModel().getRoot();
        if (!(treeNode instanceof CategoryTreeNode))
            return null;
        return (CategoryTreeNode) treeNode;
    }

    protected List<CategoryTreeNode> getSelectedTreeNodes() {
        TreePath[] treePaths = treeCategories.getSelectionPaths();
        List<CategoryTreeNode> treeNodes = new ArrayList<CategoryTreeNode>();
        for (TreePath treePath : treePaths) {
            treeNodes.add((CategoryTreeNode) treePath.getLastPathComponent());
        }
        return treeNodes;
    }

    private void selectTreePath(TreePath treePath) {
        Object selectedObject = treePath.getLastPathComponent();
        if (!(selectedObject instanceof CategoryTreeNode))
            return;
        treeCategories.expandPath(treePath);
        treeCategories.scrollPathToVisible(treePath);
        CategoryTreeNode selectedCategoryTreeNode = (CategoryTreeNode) selectedObject;
        selectTreeNode(selectedCategoryTreeNode);
        RouteConverter.getInstance().setCategoryPreference(TreePathStringConversion.toString(treePath));
    }

    private void selectTreeNode(CategoryTreeNode selected) {
        RoutesListModel routesListModel = selected.getRoutesListModel();
        if (routesListModel == null)
            return;
        tableRoutes.setModel(routesListModel);
        TableCellRenderer routesHeaderRenderer = new RoutesTableCellHeaderRenderer();
        TableColumnModel routeColumns = tableRoutes.getColumnModel();
        for (int i = 0; i < routeColumns.getColumnCount(); i++) {
            TableColumn column = routeColumns.getColumn(i);
            column.setHeaderRenderer(routesHeaderRenderer);
            if (i == 0)
                column.setMaxWidth(50);
            if (i == 2) {
                column.setPreferredWidth(100);
                column.setMaxWidth(100);
            }
        }
    }

    private RoutesListModel getRoutesListModel() {
        return (RoutesListModel) tableRoutes.getModel();
    }

    private RouteServiceOperator getOperator() {
        return RouteConverter.getInstance().getOperator();
    }

    private void addCategory() {
        final CategoryTreeNode selected = getSelectedTreeNode();
        final String name = JOptionPane.showInputDialog(RouteConverter.getInstance().getFrame(),
                MessageFormat.format(RouteConverter.getBundle().getString("add-category-label"), selected.getName()),
                RouteConverter.getInstance().getFrame().getTitle(), JOptionPane.QUESTION_MESSAGE);
        if (trim(name) == null)
            return;

        getOperator().executeOnRouteService(new RouteServiceOperator.Operation() {
            public void run() throws IOException {
                final CategoryTreeNode subCategory = selected.addSubCategory(name);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        treeCategories.expandPath(new TreePath(selected.getPath()));
                        treeCategories.scrollPathToVisible(new TreePath(subCategory.getPath()));
                    }
                });
            }
        });
    }

    private void renameCategory() {
        final CategoryTreeNode selected = getSelectedTreeNode();
        final String name = (String) JOptionPane.showInputDialog(RouteConverter.getInstance().getFrame(),
                MessageFormat.format(RouteConverter.getBundle().getString("rename-category-label"), selected.getName()),
                RouteConverter.getInstance().getFrame().getTitle(), JOptionPane.QUESTION_MESSAGE, null, null, selected.getName());
        if (trim(name) == null)
            return;

        getOperator().executeOnRouteService(new RouteServiceOperator.Operation() {
            public void run() throws IOException {
                selected.renameCategory(name);
            }
        });
    }

    private void deleteCategory() {
        final List<CategoryTreeNode> categories = getSelectedTreeNodes();
        StringBuilder categoryNames = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            CategoryTreeNode categoryTreeNode = categories.get(i);
            categoryNames.append(categoryTreeNode.getName());
            if (i < categories.size() - 1)
                categoryNames.append(", ");
        }

        int confirm = JOptionPane.showConfirmDialog(RouteConverter.getInstance().getFrame(),
                MessageFormat.format(RouteConverter.getBundle().getString("confirm-delete-category"), categoryNames),
                RouteConverter.getInstance().getFrame().getTitle(), JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        getOperator().executeOnRouteService(new RouteServiceOperator.Operation() {
            public void run() throws IOException {
                for (CategoryTreeNode category : categories) {
                    category.delete();
                }
            }
        });
    }


    private void addFileToCatalog() {
        CategoryTreeNode categoryTreeNode = getSelectedTreeNode();

        JFileChooser chooser = Constants.createJFileChooser();
        chooser.setDialogTitle(RouteConverter.getBundle().getString("add-file"));
        chooser.setSelectedFile(RouteConverter.getInstance().getUploadRoutePreference());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        int open = chooser.showOpenDialog(RouteConverter.getInstance().getFrame());
        if (open != JFileChooser.APPROVE_OPTION)
            return;

        final File[] selected = chooser.getSelectedFiles();
        if (selected == null || selected.length == 0)
            return;

        RouteConverter.getInstance().setUploadRoutePreference(selected[0]);
        addFilesToCatalog(categoryTreeNode, asList(selected));
    }

    private void addFileToCatalog(CategoryTreeNode categoryTreeNode, File file) {
        RouteConverter r = RouteConverter.getInstance();
        String path = Files.createReadablePath(file);
        String description = null;
        Double length = null;
        try {
            NavigationFileParser parser = new NavigationFileParser();
            if (parser.read(file)) {
                BaseRoute<BaseNavigationPosition, BaseNavigationFormat> route = parser.getTheRoute();
                if (route != null) {
                    description = RouteComments.createRouteDescription(route);
                    length = route.getDistance();
                }
                showAddFileToCatalog(categoryTreeNode, description, length, file);
            } else
                r.handleUnsupportedFormat(path);
        } catch (BabelException e) {
            r.handleBabelError(e);
        } catch (OutOfMemoryError e) {
            r.handleOutOfMemoryError();
        } catch (FileNotFoundException e) {
            r.handleFileNotFound(path);
        } catch (Throwable t) {
            log.severe("Cannot parse description from route " + path + ": " + t.getMessage());
            r.handleOpenError(t, path);
        }
    }

    public void addFilesToCatalog(List<File> files) {
        addFilesToCatalog(getSelectedTreeNode(), files);
    }

    protected void addFilesToCatalog(CategoryTreeNode category, List<File> files) {
        if (category == null || category.getParent() == null) {
            RouteConverter r = RouteConverter.getInstance();
            JOptionPane.showMessageDialog(r.getFrame(),
                    r.getContext().getBundle().getString("add-file-category-missing"),
                    r.getFrame().getTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (File file : files) {
            addFileToCatalog(category, file);
        }
    }

    public void addUrlToCatalog(String url) {
        addUrlToCatalog(getSelectedTreeNode(), url);
    }

    protected void addUrlToCatalog(CategoryTreeNode category, String url) {
        if (category.getParent() == null) {
            RouteConverter r = RouteConverter.getInstance();
            JOptionPane.showMessageDialog(r.getFrame(),
                    r.getContext().getBundle().getString("add-url-category-missing"),
                    r.getFrame().getTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        showAddUrlToCatalog(category, DnDHelper.extractDescription(url), DnDHelper.extractUrl(url));
    }

    private void showAddUrlToCatalog(CategoryTreeNode categoryTreeNode, String description, String url) {
        AddUrlDialog addUrlDialog = new AddUrlDialog(getOperator(), categoryTreeNode, description, url);
        addUrlDialog.pack();
        addUrlDialog.restoreLocation();
        addUrlDialog.setVisible(true);
    }

    private void showAddFileToCatalog(CategoryTreeNode categoryTreeNode, String description, Double length, File file) {
        AddFileDialog addFileDialog = new AddFileDialog(getOperator(), categoryTreeNode, description, length, file);
        addFileDialog.pack();
        addFileDialog.restoreLocation();
        addFileDialog.setVisible(true);
    }


    private void renameRoute() {
        int selectedRow = tableRoutes.getSelectedRow();
        if (selectedRow == -1)
            return;

        final CategoryTreeNode categoryTreeNode = getSelectedTreeNode();
        if (categoryTreeNode == null)
            return;

        final Route selected = getRoutesListModel().getRoute(selectedRow);
        String description = null;
        try {
            description = (String) JOptionPane.showInputDialog(RouteConverter.getInstance().getFrame(),
                    MessageFormat.format(RouteConverter.getBundle().getString("rename-route-label"), selected.getName()),
                    RouteConverter.getInstance().getFrame().getTitle(), JOptionPane.QUESTION_MESSAGE, null, null, selected.getDescription());
        } catch (IOException e) {
            getOperator().handleServiceError(e);
        }
        if (trim(description) == null)
            return;

        final String theDescription = description;
        getOperator().executeOnRouteService(new RouteServiceOperator.Operation() {
            public void run() throws IOException {
                // strange way to handle cache invalidations
                categoryTreeNode.renameRoute(selected, theDescription);
            }
        });
    }

    private void deleteRoute() {
        final int[] selectedRows = tableRoutes.getSelectedRows();
        if (selectedRows.length == 0)
            return;

        final CategoryTreeNode category = getSelectedTreeNode();
        if (category == null)
            return;

        getOperator().executeOnRouteService(new RouteServiceOperator.Operation() {
            public void run() throws IOException {
                for (int selectedRow : selectedRows) {
                    Route route = getRoutesListModel().getRoute(selectedRow);
                    // strange way to handle cache invalidations
                    category.deleteRoute(route);
                }
            }
        });
    }


    protected void moveCategory(final List<CategoryTreeNode> categories, final CategoryTreeNode parent) {
        getOperator().executeOnRouteService(new RouteServiceOperator.Operation() {
            public void run() throws IOException {
                for (CategoryTreeNode category : categories) {
                    category.moveCategory(parent);
                }
            }
        });
    }

    protected void moveRoute(final List<Route> routes, final CategoryTreeNode source, final CategoryTreeNode target) {
        getOperator().executeOnRouteService(new RouteServiceOperator.Operation() {
            public void run() throws IOException {
                for (Route route : routes) {
                    source.moveRoute(route, target);
                }
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        browsePanel = new JPanel();
        browsePanel.setLayout(new GridLayoutManager(4, 2, new Insets(3, 3, 3, 3), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        browsePanel.add(panel1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonAddCategory = new JButton();
        this.$$$loadButtonText$$$(buttonAddCategory, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("add"));
        panel1.add(buttonAddCategory, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonDeleteCategory = new JButton();
        this.$$$loadButtonText$$$(buttonDeleteCategory, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("delete"));
        panel1.add(buttonDeleteCategory, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRenameCategory = new JButton();
        this.$$$loadButtonText$$$(buttonRenameCategory, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("rename"));
        panel1.add(buttonRenameCategory, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("categories"));
        browsePanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("routes"));
        browsePanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        browsePanel.add(panel2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonAddFile = new JButton();
        this.$$$loadButtonText$$$(buttonAddFile, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("add-route-by-file"));
        panel2.add(buttonAddFile, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        buttonDeleteRoute = new JButton();
        this.$$$loadButtonText$$$(buttonDeleteRoute, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("delete"));
        panel2.add(buttonDeleteRoute, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRenameRoute = new JButton();
        this.$$$loadButtonText$$$(buttonRenameRoute, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("rename"));
        panel2.add(buttonRenameRoute, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAddUrl = new JButton();
        this.$$$loadButtonText$$$(buttonAddUrl, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("add-route-by-url"));
        panel2.add(buttonAddUrl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonLogin = new JButton();
        this.$$$loadButtonText$$$(buttonLogin, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("login"));
        buttonLogin.setVisible(false);
        panel2.add(buttonLogin, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        browsePanel.add(scrollPane1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableRoutes = new JTable();
        scrollPane1.setViewportView(tableRoutes);
        final JScrollPane scrollPane2 = new JScrollPane();
        browsePanel.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        treeCategories = new JTree();
        treeCategories.setRootVisible(false);
        scrollPane2.setViewportView(treeCategories);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return browsePanel;
    }

    private class TableDragHandler extends TransferHandler {
        public int getSourceActions(JComponent comp) {
            return MOVE;
        }

        protected Transferable createTransferable(JComponent c) {
            int[] selectedRows = tableRoutes.getSelectedRows();
            List<Route> selectedRoutes = new ArrayList<Route>();
            for (int selectedRow : selectedRows) {
                Route route = getRoutesListModel().getRoute(selectedRow);
                selectedRoutes.add(route);
            }
            return new RouteSelection(selectedRoutes);
        }
    }

    private class TreeDragAndDropHandler extends TransferHandler {
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        protected Transferable createTransferable(JComponent c) {
            return new CategorySelection(getSelectedTreeNodes());
        }

        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(CategorySelection.categoryFlavor) ||
                    support.isDataFlavorSupported(RouteSelection.routeFlavor) ||
                    support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ||
                    support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @SuppressWarnings("unchecked")
        public boolean importData(TransferSupport support) {
            JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
            TreePath path = dropLocation.getPath();
            CategoryTreeNode target = (CategoryTreeNode) path.getLastPathComponent();
            try {
                Transferable t = support.getTransferable();
                if (support.isDataFlavorSupported(CategorySelection.categoryFlavor)) {
                    Object data = t.getTransferData(CategorySelection.categoryFlavor);
                    if (data != null) {
                        List<CategoryTreeNode> categories = (List<CategoryTreeNode>) data;
                        moveCategory(categories, target);
                        return true;
                    }
                }

                if (support.isDataFlavorSupported(RouteSelection.routeFlavor)) {
                    Object data = t.getTransferData(RouteSelection.routeFlavor);
                    if (data != null) {
                        List<Route> routes = (List<Route>) data;
                        CategoryTreeNode source = getSelectedTreeNode();
                        moveRoute(routes, source, target);
                        return true;
                    }
                }

                if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    Object data = t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (data != null) {
                        List<File> files = (List<File>) data;
                        addFilesToCatalog(target, files);
                        return true;
                    }
                }

                if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    Object data = t.getTransferData(DataFlavor.stringFlavor);
                    if (data != null) {
                        String url = (String) data;
                        addUrlToCatalog(target, url);
                        return true;
                    }
                }
            } catch (UnsupportedFlavorException e) {
                // intentionally left empty
            } catch (IOException e) {
                // intentionally left empty
            }
            return false;
        }
    }

}
