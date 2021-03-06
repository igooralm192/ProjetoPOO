package com.example.igor.projetopoo.activity.search;

import com.example.igor.projetopoo.database.Database;
import com.example.igor.projetopoo.entities.Category;
import com.example.igor.projetopoo.entities.Product;
import com.example.igor.projetopoo.exception.ConnectionException;
import com.example.igor.projetopoo.exception.DatabaseException;
import com.example.igor.projetopoo.helper.AsyncDownload;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchMVP.PresenterOps, SearchMVP.ReqPresenterOps {
    private SearchActivity activity;
    private SearchMVP.ReqViewOps reqViewOps;
    private SearchMVP.ModelOps modelOps;

    public SearchPresenter(SearchActivity activity, Database database){
        this.activity = activity;
        this.reqViewOps = activity;
        this.modelOps = new SearchModel(activity, this, database);

    }

    /**
     *  Executa uma AsyncTask (tarefas simultâneas), determinando que a View mostre o símbolo de
     *  carregamento, enquanto requisita à Model a lista de produtos e/ou categorias e depois desabilita o
     *  símbolo que estava sendo mostrado, após conclusão da pesquisa.
     */
    @Override
    public void getResultList(final String query) {
        AsyncDownload asyncDownload = new AsyncDownload(new AsyncDownload.OnAsyncDownloadListener() {
            @Override
            public void onPreExecute() {
                reqViewOps.showProgressBar(true);

            }

            @Override
            public Object doInBackground(Object... objects) {

                try {
                    // Formata a string de pesquisa recebida
                    char lastChar = query.charAt(query.length()-1);
                    char letters[] = query.toCharArray();

                    for (int i =0; i < letters.length; i++){
                        if (letters[i] == ' '){
                            letters[i+1] = Character.toUpperCase(letters[i+1]);
                        }
                    }

                    if (letters.length == 1) lastChar = Character.toUpperCase(lastChar);

                    letters[0] = Character.toUpperCase(letters[0]);
                    String lowerbound = new String(letters);

                    letters[query.length()-1] = ++lastChar;
                    String upperbound = new String(letters);

                    modelOps.resultListRequest(lowerbound, upperbound);
                } catch (ConnectionException e) {
                    e.connectionFail(SearchPresenter.this, query);
                } catch (DatabaseException e) {
                    e.failReadData();
                }

                return null;
            }

            @Override
            public void onPostExecute(Object object) {
                reqViewOps.showProgressBar(false);
            }
        });

        asyncDownload.execute();
    }


    /**
     * Separa em duas listas o(s) resultado(s) da consulta do banco de dados,
     * de acordo com o tipo do resultado, e retorna  essas listas para a View
     */
    @Override
    public void onReturnedResultList(List<Object> objects) {
        List<Category> categoryList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();

        for (Object object: objects) {
            if (object instanceof Category) {
                categoryList.add((Category) object);
            }else {
                productList.add((Product) object);
            }
        }
        reqViewOps.showResults(categoryList, productList);
    }

}
