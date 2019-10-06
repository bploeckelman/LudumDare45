package lando.systems.ld45.utils;

public enum UIAssetType {
      toychest_panel              ("ui-toychest-panel")
    , toychest_panel_inset        ("ui-toychest-panel-inset")
    , upgrade_panel               ("ui-upgrade-panel")
    , upgrade_panel_inset         ("ui-upgrade-panel-inset")
    ;
    public final String fileName;
    UIAssetType(String fileName) {
        this.fileName = fileName;
    }
}
