package temp.dao;

import server.transformation.Transformer;

import java.util.List;

public interface TransformerDAO extends GenericDAO<Transformer, Long> {

    Transformer get(Long id);

    Transformer get(String id);

    /**
     * List all Transformers available in the repository.
     * @return A list of all Transformers in the repository.
     */
    List<Transformer> list();

}