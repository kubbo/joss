package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.instructions.ListInstructions;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.command.core.*;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.Collection;

import static nl.tweeenveertig.openstack.command.core.CommandUtil.convertResponseToString;

public class ListContainersCommand extends AbstractSecureCommand<HttpGet, Collection<String>> {

    public static final int MAX_PAGE_SIZE = 9999; // http://docs.openstack.org/api/openstack-object-storage/1.0/content/list-objects.html

    public ListContainersCommand(Account account, HttpClient httpClient, AccessImpl access, ListInstructions listInstructions) {
        super(account, httpClient, access);
        modifyURI(listInstructions.getQueryParameters());
    }

    @Override
    protected Collection<String> getReturnObject(HttpResponse response) throws IOException {
        return convertResponseToString(response);
    }

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}
