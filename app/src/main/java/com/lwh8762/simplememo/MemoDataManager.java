package com.lwh8762.simplememo;

import android.util.Log;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by W on 2016-11-21.
 */
public class MemoDataManager {

    private static String directory = "";
    private static String fileName = "data.sim";

    private static ArrayList<String> memoList = null;

    public static void setup(String directory) {
        MemoDataManager.directory = directory;
        MemoDataManager.memoList = new ArrayList<>();

        try {
            File file = new File(MemoDataManager.directory + File.separator + MemoDataManager.fileName);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(MemoDataManager.directory + File.separator + MemoDataManager.fileName));
                String read = null;
                while ((read = br.readLine()) != null) {
                    MemoDataManager.memoList.add(read);
                }
            }
            Log.i("s", "" + MemoDataManager.getSize());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setData() {
        if (MemoDataManager.memoList.size() == 0) {
            File file = new File(directory + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
            return;
        }else {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(MemoDataManager.directory + File.separator + MemoDataManager.fileName));
                bw.write(memoList.get(0));
                for (int i = 1;i < MemoDataManager.memoList.size();i ++) {
                    bw.newLine();
                    bw.write(MemoDataManager.memoList.get(i));
                }
                bw.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addMemo(String memo) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(MemoDataManager.directory + File.separator + MemoDataManager.fileName, true));
            if (MemoDataManager.getSize() != 0) {
                bw.newLine();
            }
            bw.write(memo);
            bw.close();
            MemoDataManager.memoList.add(memo);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setMemo(int position, String memo) {
        MemoDataManager.memoList.set(position, memo);
        setData();
    }

    public static void setMemoList(ArrayList<String> memoList) {
        MemoDataManager.memoList = memoList;
    }

    public static void removeMemo(int position) {
        MemoDataManager.memoList.remove(position);
        setData();
    }

    public static int getSize() {
        return MemoDataManager.memoList.size();
    }

    public static String getMemo(int position) {
        return MemoDataManager.memoList.get(position);
    }

    public static String getLastMemo() {
        return MemoDataManager.memoList.get(MemoDataManager.getSize() - 1);
    }

    public static ArrayList<String> getMemoList() {
        return memoList;
    }
}
