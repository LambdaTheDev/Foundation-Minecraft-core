package pl.lambda.foundationminecraft.utils.exceptions;

public class MemberIsNullException extends Exception
{
    public MemberIsNullException(){}
    public MemberIsNullException(String memberID)
    {
        System.err.println("MemberID={" + memberID + "}");
    }
}
