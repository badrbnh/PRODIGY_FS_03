import { createSlice, createAsyncThunk} from "@reduxjs/toolkit";
import authService from "./authService";

const initialState = {
    user: JSON.parse(localStorage.getItem('user')) || null,
    isError: false,
    isSuccess: false,
    isLoading: false,
    message: "",
}

export const register = createAsyncThunk(
    "auth/register",
    async (userData, thunkAPI) => {
        try {
            return await authService.register(userData);
        } catch (error) {
            const message = error.response?.data?.message || error.message || error.toString();
            return thunkAPI.rejectWithValue({message});

        }
    }
)

export const login = createAsyncThunk(
    "auth/login",
    async (userData, thunkAPI) => {
        try {
            return await authService.login(userData);
        } catch (error) {
            const message = error.response?.data?.message || error.message || error.toString();
            return thunkAPI.rejectWithValue({message});

        }
    }
)
export const logout = createAsyncThunk(
    "auth/logout",
    async () => {
        await authService.logout();
    }
)


export const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        reset: (state) => {
            state.user = null;
            state.isError = false;
            state.isSuccess = false;
            state.isLoading = false;
            state.message = "";
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(register.pending, (state) => {
                state.isLoading = true;
            })
            .addCase(register.fulfilled, (state, action) => {
                state.isLoading = false;
                state.isSuccess = true;
                state.user = action.payload;
            })
            .addCase(register.rejected, (state, action) => {
                state.isLoading = false;
                state.isSuccess = false;
                state.isError = true;
                state.message = action.payload.message || "Registration failed";
                state.user = null;
            })
            .addCase(login.pending, (state) => {
                state.isLoading = true;
            })
            .addCase(login.fulfilled, (state, action) => {
                state.isLoading = false;
                state.isSuccess = true;
                state.user = action.payload;
            })
            .addCase(login.rejected, (state, action) => {
                state.isLoading = false;
                state.isSuccess = false;
                state.isError = true;
                state.message = action.payload.message || "login failed";
                state.user = null;
            })
            .addCase(logout.fulfilled, (state) => {
                state.user = null;
            })
    }
})



export const { reset } = authSlice.actions;
export default authSlice.reducer;
