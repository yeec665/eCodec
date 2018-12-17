package indi.hiro.common.ds.primitive;


public class ListenedFlagPole implements FlagPole, java.io.Serializable {

    private static final int FLAG_LIS_ARRAY_LEN = 8;

    private transient FlagPoleListenedItem[] listenedItems = new FlagPoleListenedItem[FLAG_LIS_ARRAY_LEN];

    private transient int mf;

    public ListenedFlagPole() {

    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        listenedItems = new FlagPoleListenedItem[FLAG_LIS_ARRAY_LEN];
    }

    @Override
    public void setFlagValue(int f) {
        flagChange(f);
    }

    @Override
    public int getFlagValue() {
        return mf;
    }

    @Override
    public void addFlag(int f) {
        flagChange(mf | f);
    }

    @Override
    public void addFlagByIndex(int i) {
        flagChange(mf | (1 << i));
    }

    @Override
    public void removeFlag(int f) {
        flagChange(mf & ~f);
    }

    @Override
    public void removeFlagByIndex(int i) {
        flagChange(mf & ~(1 << i));
    }

    @Override
    public boolean hasFlag(int f) {
        return (mf & f) != 0;
    }

    @Override
    public boolean hasFlags(int f) {
        return (mf | ~f) == 0xFFFFFFFF;
    }

    @Override
    public boolean hasFlagByIndex(int i) {
        return (mf & (1 << i)) != 0;
    }

    public void addFlagListener(int flag, FlagListener listener) {
        FlagPoleListenedItem item = new FlagPoleListenedItem(flag, listener);
        if ((flag & 0x0000FFFF) != 0) {
            if ((flag & 0x000000FF) != 0) {
                if ((flag & 0x0000000F) != 0) {
                    addFlagListener(item, 0);
                }
                if ((flag & 0x000000F0) != 0) {
                    addFlagListener(item, 1);
                }
            }
            if ((flag & 0x0000FF00) != 0) {
                if ((flag & 0x00000F00) != 0) {
                    addFlagListener(item, 2);
                }
                if ((flag & 0x0000F000) != 0) {
                    addFlagListener(item, 3);
                }
            }
        }
        if ((flag & 0xFFFF0000) != 0) {
            if ((flag & 0x00FF0000) != 0) {
                if ((flag & 0x000F0000) != 0) {
                    addFlagListener(item, 4);
                }
                if ((flag & 0x00F00000) != 0) {
                    addFlagListener(item, 5);
                }
            }
            if ((flag & 0xFF000000) != 0) {
                if ((flag & 0x0F000000) != 0) {
                    addFlagListener(item, 6);
                }
                if ((flag & 0xF0000000) != 0) {
                    addFlagListener(item, 7);
                }
            }
        }
    }

    private void addFlagListener(FlagPoleListenedItem item, int index) {
        item.next = listenedItems[index];
        listenedItems[index] = item;
    }

    private void flagChange(int nf) {
        int change = mf ^ nf;
        if (change == 0) {
            return;
        }
        mf = nf;
        if ((change & 0x0000FFFF) != 0) {
            if ((change & 0x000000FF) != 0) {
                if ((change & 0x0000000F) != 0) {
                    flagChange(change, listenedItems[0]);
                }
                if ((change & 0x000000F0) != 0) {
                    flagChange(change, listenedItems[1]);
                }
            }
            if ((change & 0x0000FF00) != 0) {
                if ((change & 0x00000F00) != 0) {
                    flagChange(change, listenedItems[2]);
                }
                if ((change & 0x0000F000) != 0) {
                    flagChange(change, listenedItems[3]);
                }
            }
        }
        if ((change & 0xFFFF0000) != 0) {
            if ((change & 0x00FF0000) != 0) {
                if ((change & 0x000F0000) != 0) {
                    flagChange(change, listenedItems[4]);
                }
                if ((change & 0x00F00000) != 0) {
                    flagChange(change, listenedItems[5]);
                }
            }
            if ((change & 0xFF000000) != 0) {
                if ((change & 0x0F000000) != 0) {
                    flagChange(change, listenedItems[6]);
                }
                if ((change & 0xF0000000) != 0) {
                    flagChange(change, listenedItems[7]);
                }
            }
        }
    }

    private void flagChange(int change, FlagPoleListenedItem node) {
        while (node != null) {
            if ((change & node.flag) != 0) {
                node.listener.onFlagChange(this);
            }
            node = node.next;
        }
    }

    class FlagPoleListenedItem {

        FlagPoleListenedItem next;

        int flag;

        FlagListener listener;

        FlagPoleListenedItem(int flag, FlagListener listener) {
            this.flag = flag;
            this.listener = listener;
        }
    }
}
