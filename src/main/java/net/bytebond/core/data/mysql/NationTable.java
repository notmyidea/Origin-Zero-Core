package net.bytebond.core.data.mysql;

import net.bytebond.core.data.NationData;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleFlatDatabase;

public class NationTable extends SimpleFlatDatabase<NationData> {


    @Override
    protected void onLoad(SerializedMap map, NationData data) {

    }

    @Override
    protected SerializedMap onSave(NationData data) {
        return null;
    }
}
