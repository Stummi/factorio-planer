package org.stummi.factorio.gui;

import org.stummi.factorio.data.Entity;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NameAndIconTableCell<S, T extends Entity> extends TableCell<S, T> {
	private final JFXImageFactory factory;

	private ComboBox<T> comboBox;
	private ImageView imageView = new ImageView();

	private ObservableList<T> data;

	public NameAndIconTableCell(JFXImageFactory factory, ObservableList<T> data) {
		this.factory = factory;
		this.data = data;
		imageView = new ImageView();
	}

	@Override
	public void startEdit() {
		if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
			return;
		}

		if (comboBox == null) {
			comboBox = createComboBox();
		}

		comboBox.hide();
		comboBox.setValue(getItem());
		comboBox.getSelectionModel().select(getItem());

		super.startEdit();
		setGraphic(comboBox);
		comboBox.show();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setGraphic(imageView);
	}

	@Override
	public void commitEdit(T newValue) {
		super.commitEdit(newValue);
		setGraphic(imageView);
	};

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			return;
		} else if (getGraphic() == null) {
			setGraphic(imageView);
		}

		Image icon = item == null ? JFXImageFactory.DEFAULT_IMAGE : factory.getImage(item.getIconName());
		imageView.setImage(icon);
	}

	private ComboBox<T> createComboBox() {
		NameAndIconComboBox<T> cbox = new NameAndIconComboBox<>(factory);
		cbox.setItems(data);
		cbox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
			if (isEditing()) {
				commitEdit(newValue);
			}
		});
		return cbox;
	}
}
