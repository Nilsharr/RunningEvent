export const required = (value) => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

export const passwordLength = (value) => {
    if (value.length < 6 || value.length > 30) {
        return (
            <div className="alert alert-danger" role="alert">
                Password must be between 6 and 30 characters long!
            </div>
        );
    }
};