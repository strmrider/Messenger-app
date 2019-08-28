package com.example.messenger.viewmodel.RegistrationViewModel;

public enum RegistrationAction {
    Register(0),
    ChangeAccount(1);

    private int type;
    RegistrationAction(int type)
    {
        this.type = type;
    }

    public int getValue()
    {
        return type;
    }

    public static RegistrationAction fromInt(int type)
    {
        if(type == 0)
                return Register;
        if (type == 1)
            return ChangeAccount;
        else return null;
    }
}
