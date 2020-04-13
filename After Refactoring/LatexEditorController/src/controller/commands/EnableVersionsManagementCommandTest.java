package controller.commands;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import controller.LatexEditorController;
import model.DocumentManager;
import model.VersionsManager;
import model.strategies.StableVersionsStrategy;
import model.strategies.VersionsStrategy;
import model.strategies.VolatileVersionsStrategy;
import view.LatexEditorView;

class EnableVersionsManagementCommandTest {
	private LatexEditorController latexEditorController;
	private LatexEditorView latexEditorView;
	private DocumentManager documentManager;
	private VersionsManager versionsManager;
	private CreateCommand createCommand;
	private EditCommand editCommand;
	private EnableVersionsManagementCommand enableCommand;

	public EnableVersionsManagementCommandTest() throws IOException {
		latexEditorController = new LatexEditorController();
		latexEditorView = new LatexEditorView(latexEditorController);
		documentManager = new DocumentManager();
		versionsManager = latexEditorController.getVersionsManager();
		createCommand = new CreateCommand(documentManager, versionsManager, latexEditorController);
		editCommand = new EditCommand(latexEditorController);
		enableCommand = new EnableVersionsManagementCommand(versionsManager);
	}
	
	@Test
	void testVolatile() {
		VersionsStrategy strategy = new VolatileVersionsStrategy();
		versionsManager.setStrategy(strategy);
		latexEditorController.getCurrentDocument().setDocumentType("emptyTemplate");
		createCommand.execute();
		latexEditorView.setStrategy("volatile");
		enableCommand.execute();
		latexEditorView.setTypedText("test edit contents\n");
		editCommand.execute();
		String actualContents = latexEditorController.getCurrentDocument().getContents();
		String contents = strategy.getVersion().getContents();
		
		assertEquals(contents, actualContents);
	}
	@Test
	void testStable() {
		VersionsStrategy strategy = new StableVersionsStrategy();
		versionsManager.setStrategy(strategy);
		
		latexEditorController.getCurrentDocument().setDocumentType("emptyTemplate");
		createCommand.execute();
		latexEditorView.setStrategy("stable");
		enableCommand.execute();
		latexEditorView.setTypedText("test edit contents\n");
		editCommand.execute();
		String actualContents = latexEditorController.getCurrentDocument().getContents();
		String contents = strategy.getVersion().getContents();
		assertEquals(contents, actualContents);
	}
}
