package spring.boot.endterm.service.center;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.endterm.entity.Center;
import spring.boot.endterm.exceptions.CustomNotFoundException;
import spring.boot.endterm.repository.CenterRepository;

@Service
public class CenterServiceImpl implements CenterService{

    private final CenterRepository repository;

    @Autowired
    public CenterServiceImpl(CenterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Center findById(Long id) {
        return repository.findById(id).orElseThrow(()->new CustomNotFoundException(String.format("" +
                "Center with id : %s does not exist" , id)));
    }
}
