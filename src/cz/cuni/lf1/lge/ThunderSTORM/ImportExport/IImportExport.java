package cz.cuni.lf1.lge.ThunderSTORM.ImportExport;

import cz.cuni.lf1.lge.ThunderSTORM.IModule;
import cz.cuni.lf1.lge.ThunderSTORM.results.IJResultsTable;
import java.io.IOException;
import java.util.Vector;

public interface IImportExport extends IModule {
    
    public String getName();
    public void importFromFile(String fp, IJResultsTable rt) throws IOException;
    public void exportToFile(String fp, IJResultsTable rt, Vector<String> columns) throws IOException;
    public String getSuffix();  // filename suffix
   
}