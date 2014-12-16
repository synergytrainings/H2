import java.sql.Connection;
import java.sql.SQLException;

import com.synisys.test.database.DatabaseCreator;
import com.synisys.test.database.DoubleRange;
import com.synisys.test.database.IntRange;
import com.synisys.test.database.StringRange;
import com.synisys.test.database.metadata.ColumnType;
import com.synisys.test.database.metadata.Table;
import com.synisys.test.database.metadata.TableColumn;

public class SampleDb1 {

	private void build(DatabaseCreator databaseCreator, Connection connection) throws SQLException {
		Table projectTable = Table.createTable("Project");
		TableColumn projectId = projectTable.createPrimaryKeyColumn("ProjectId", ColumnType.INT);
		TableColumn projectName = projectTable.createColumn("Name", ColumnType.STRING1000);

		Table sectorTable = Table.createTable("Sector");
		TableColumn sectorId = projectTable.createPrimaryKeyColumn("SectorId", ColumnType.INT);
		TableColumn sectorName = projectTable.createColumn("Name", ColumnType.STRING1000);

		Table projectSectorTable = Table.createTable("ProjectSector");
		TableColumn projectSector_projectId = projectTable.createForeignKeyColumn("ProjectId", projectId);
		TableColumn projectSector_sectorId = projectTable.createForeignKeyColumn("SectorId", sectorId);
		TableColumn commitedAmount = projectTable.createColumn("commitedAmount", ColumnType.DECIMAL6_36);

		databaseCreator.build(projectTable, sectorTable, projectSectorTable);

		databaseCreator.initTableData(projectTable, connection, new IntRange(projectId, 1, 100), new StringRange(projectName,
				"Project", 1, 100));
		databaseCreator.initTableData(sectorTable, connection, new IntRange(sectorId, 1, 100), new StringRange(sectorName,
				"Sector1", 1, 10));

		databaseCreator.initTableData(projectSectorTable, connection, new IntRange(projectId, 1, 100), new IntRange(sectorId, 1, 10),
				new DoubleRange(commitedAmount, 100.0, 200.0, 300.0, 400.0, 500.0));
	}
}
